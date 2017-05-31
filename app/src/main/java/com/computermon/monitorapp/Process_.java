package com.computermon.monitorapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sam on 5/7/2017.
 */
@IgnoreExtraProperties
public class Process_ {
    private String pName;
    private String piD;

    public Process_() {
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getPiD() {
        return piD;
    }

    public void setPiD(String piD) {
        this.piD = piD;
    }


}
