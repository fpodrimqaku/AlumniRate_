package com.mindorks.framework.mvvm.data.model.firebase;

import android.net.Uri;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


public class User {
    private String First;
    private String Last;
    private String Username;
    private Boolean isEmailVerified;

    private String Title;
    private String photoUri;
    private String email;
    private String UID;


    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getFirst() {
        return First;
    }

    public void setFirst(String first) {
        First = first;
    }

    public String getLast() {
        return Last;
    }

    public void setLast(String last) {
        Last = last;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPhotoUrl() {
        return photoUri;
    }

    public void setPhotoUrl(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return "" + getFirst() + ' ' + getLast();
    }

    ;


    public HashMap<String, String> validateFields() {
        HashMap<String, String> errorList = new HashMap<>();

        if (getFirst() == null || getFirst().equals("")) {
            errorList.put("Plotësoni fushën ", "Emri");
            return errorList;
        }
        if (getLast() == null || getLast().equals("")) {
            errorList.put("Plotësoni fushën ", "Mbiemri");
            return errorList;
        }
        if (getEmail() == null || getEmail().equals("")) {
            errorList.put("Plotësoni fushën ", "Emaili");
            return errorList;
        }

        if (getEmail() == null || !isValidEmail(getEmail())) {
            errorList.put("Emaili i dhënë është jo valid", "");
            return errorList;
        }


        if (getTitle() == null || getTitle().equals("")) {
            errorList.put("Plotësoni fushën ", "Titulli");
            return errorList;
        }

        if (getPhotoUrl() == null || getPhotoUrl().equals("")) {
            errorList.put("Ngarkoni një foto të përdoruesit ", "");
            return errorList;
        }


        return errorList;
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(regex);
    }

}
