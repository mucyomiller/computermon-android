package com.computermon.monitorapp;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sam on 4/25/2017.
 */
@IgnoreExtraProperties
public class Process {

    String mac;
 List<ProcessList> process =new ArrayList<>();

    public Process() {
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public List<ProcessList> getProcess() {
        return process;
    }

    public void setProcess(List<ProcessList> process) {
        this.process = process;
    }
}

