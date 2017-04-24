package com.computermon.monitorapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sam on 4/24/2017.
 */
@IgnoreExtraProperties
public class Pc {
    public String email;
    public String mac;

    public Pc() {
    }

    public String getEmail() {
        return email;
    }

    public Pc(String email, String mac) {
        this.email = email;
        this.mac = mac;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
