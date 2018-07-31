package android.dms.aut.ac.nz.soteriaalarm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/* This class handle the Preferences screen, where the user enters
/  or edits their contact information
/  @author Nigel
*/

public class PreferencesActivity extends AppCompatActivity {
    private EditText user;
    private EditText contName;
    private EditText contNum;
    private TextView contCheck;
    private EmergencyContact eCont;
    private ContactOps contOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        user = (EditText) findViewById(R.id.usernameEntry);
        contName = (EditText) findViewById(R.id.contactName);
        contNum = (EditText) findViewById(R.id.numberEntry);
        contCheck = (TextView)findViewById(R.id.contCheck);
        contOps = new ContactOps(this);


        try { //Checks to see if Contact Info is set.
            eCont = contOps.readContact();
            user.setText(eCont.getUsername());
            contName.setText(eCont.getContactName());
            contNum.setText(eCont.getContactPhone());
            contCheck.setText("Contact Info Configured!.");
        } catch(NullPointerException err) {
            contCheck.setText("CONTACT INFO NOT SET.");
            contCheck.setTextColor(Color.RED);
        }
    }

    public void goBack(View v) { //Sends user back to the Main page.
        PreferencesActivity.super.onBackPressed();
    }

    public boolean inputsEntered(String... contact) { //Checks that the string inputs entered are not null
        for (String s : contact)
            if (s.isEmpty() || s == null) {
                return false;
            }
        return true;
    }

    private void toastMaker(String tMsg) { //Simplifies the ToastMaker function
        Toast.makeText(getApplicationContext(), tMsg, Toast.LENGTH_SHORT).show();
    }

    public void formValidationAndSend(View v) { //Validates the form, and Stores ContactInfo
        String un = user.getText().toString();
        String contactName = contName.getText().toString();
        String contactNumber = contNum.getText().toString();

        try {
            Integer.parseInt(contactNumber);
        } catch(NumberFormatException err) {
            toastMaker("Please enter your Contact's Phone Number");
            contactNumber = null;
        }

        if (inputsEntered(un, contactName, contactNumber) == true) {
            EmergencyContact newContact = new EmergencyContact(un, contactName, contactNumber);
            contOps.saveContact(newContact);
            toastMaker("Emergency Contact Stored!");
        } else {
            toastMaker("Please fill in all fields!");
        }
    }
}
