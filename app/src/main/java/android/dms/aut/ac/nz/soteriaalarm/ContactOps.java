package android.dms.aut.ac.nz.soteriaalarm;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* This class handles the Loading and Saving of Contact Information to the device.
/  @author Nigel
*/

public class ContactOps {
    private Context parent;
    private FileInputStream fileIn;
    private FileOutputStream fileOut;
    private ObjectInputStream objectIn;
    private ObjectOutputStream objectOut;
    private String filePath;
    private final String SAVEDCONTACT = "SC";

    public ContactOps(Context c){
        parent = c;
    }

    public EmergencyContact readContact(){ //Reads in Contact File
        EmergencyContact contact = null;
        try {
            filePath = parent.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + SAVEDCONTACT;
            fileIn = new FileInputStream(filePath);
            objectIn = new ObjectInputStream(fileIn);
            contact = (EmergencyContact)objectIn.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return contact;
    }

    public void saveContact(EmergencyContact outContact){ //Saves Contact file
        try {
            filePath = parent.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + SAVEDCONTACT;
            fileOut = new FileOutputStream(filePath);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(outContact);
            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
