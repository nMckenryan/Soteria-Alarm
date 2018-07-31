package android.dms.aut.ac.nz.soteriaalarm;

/* @author Nigel Mckenzie-Ryan
// This class holds a simple object holding the contact's name and number
*/

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class EmergencyContact implements Serializable { //implements Parcelable {
    private String username;
    private String contactName;
    private String contactPhone;

    public EmergencyContact(String username, String contactName, String contactPhone) {
        this.username = username;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
    }

    public String getUsername() {
        return username;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    @Override
    public String toString() {
        return  username + ':' + contactName + ':' + contactPhone;
    }
}