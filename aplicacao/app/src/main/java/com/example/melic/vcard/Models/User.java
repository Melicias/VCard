package com.example.melic.vcard.Models;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements Serializable {
    private long nrTelemovel;
    private String auth_key;

    private static String FILE_NAME = "user";

    public User(String auth_key, long nrTelemovel){
        this.auth_key = auth_key;
        this.nrTelemovel = nrTelemovel;
    }

    public User(){

    }

    public String getAuth_key(){
        return this.auth_key;
    }

    public User getUserFromFile(Context context){
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            User user = (User) is.readObject();
            is.close();
            fis.close();
            return user;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new User();
    }

    public void saveUserInFile(Context context){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUserFile(Context context){
        context.deleteFile(FILE_NAME);
    }

    public Long getNrTelemovel(){return this.nrTelemovel;}

}
