package com.computermon.monitorapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sam on 6/9/2017.
 */

@IgnoreExtraProperties
public class Closed {
    private String pName;
    private String mac;

    public Closed() {
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
