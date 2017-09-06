package com.computermon.monitorapp;

import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 4/25/2017.
 */
@IgnoreExtraProperties
public class Process {
    private String mac;
    private List<Process_> process = null;

    public Process() {
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public List<Process_> getProcess() {
        return process;
    }

    public void setProcess(List<Process_> process) {
        this.process = process;
    }

}

