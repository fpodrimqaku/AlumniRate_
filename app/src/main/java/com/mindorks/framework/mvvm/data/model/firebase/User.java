package com.mindorks.framework.mvvm.data.model.firebase;

import android.net.Uri;



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


}
