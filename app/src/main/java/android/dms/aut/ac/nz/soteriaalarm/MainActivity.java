package android.dms.aut.ac.nz.soteriaalarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/* This class retrieves the user's current location, shows the GPS location service
/ Status, alerts the user if contact information has not been set, and triggers the alarm.
/  @author Nigel
*/
public class MainActivity extends AppCompatActivity implements LocationListener {

    private EmergencyContact savedContact;
    private TextView coordView;
    private TextView gpsStatus;
    private TextView contactView;
    private ContactOps contOps;
    private Location coordinates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordView = (TextView)findViewById(R.id.cordView);
        gpsStatus = (TextView)findViewById(R.id.gpsStatus);
        contactView = (TextView)findViewById(R.id.contactConfirm);
        contOps = new ContactOps(this);

        checkLocationService();
        checkContact();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationService();
        getLocat();
        checkContact();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkLocationService();
        getLocat();
        checkContact();
    }

    public void toPreferences(View view) { //Handles transition to Preferences Screen.
        startActivity(new Intent(this, PreferencesActivity.class));
    }

    public void checkLocationService() { //Displays TextView indicating whether Location Service is available
        if(checkGpsConnection(this) == true) {
            gpsStatus.setText("Location Service Available");
            getLocat();
        } else {
            gpsStatus.setText("Location Service Unavailable");
            gpsStatus.setTextColor(Color.RED);
        }
    }

    public void checkContact() { //Checks Contact is Set
        try {
            savedContact = contOps.readContact();
            contactView.setText("Contact Information Set");
        } catch(NullPointerException err) {
            contactView.setText("WARNING: Set Contact Information in Preferences before using!");
        }
    }

    public void getLocat() { //Gets Location data
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String gpsProvider = LocationManager.GPS_PROVIDER;

        try {
            lm.requestLocationUpdates(gpsProvider, 0, 0, this);
            coordinates = lm.getLastKnownLocation(gpsProvider);
            if(coordinates != null) {
                coordView.setText("Location: " + coordinates.getLongitude() + ", " + coordinates.getLatitude());
            }
        } catch(SecurityException err) {
            System.out.println(err);
        }
    }

    public static boolean checkGpsConnection(Context context) { //Checks for Location Service availablity
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public void sendSMS(View v) { //Sends SMS
        String emergencyMessage = savedContact.getContactName() + "!" + savedContact.getUsername() + " needs help! " +
                "Last Known Location: https://www.google.com/maps/?q=" + coordinates.getLatitude() + "," + coordinates.getLongitude();
        try {
            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(savedContact.getContactPhone(), null, emergencyMessage, pi, null);
            Toast.makeText(getApplicationContext(), "Alarm Triggered!", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Alarm Failed! Make sure Contact is set!", Toast.LENGTH_SHORT).show();
            System.err.println(ex);
        }
    }

    public void onLocationChanged(Location location){ //handles location updates if location changes.
        coordView.setText("Location: " + location.getLongitude() + location.getLatitude());
        Log.w(MainActivity.class.getName(),
         "Location: " + location);
    }

    //Following classes are not used, but are required for the GPS listener.

    // implementation of onStatusChanged method
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    // implementation of onProviderDisabled method
    public void onProviderDisabled(String provider) {

    }

    // implementation of onProviderEnabled method
    public void onProviderEnabled(String provider){
    }
}
