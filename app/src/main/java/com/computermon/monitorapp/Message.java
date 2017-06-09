package com.computermon.monitorapp;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sam on 6/9/2017.
 */
@IgnoreExtraProperties
public class Message {

        public String notification;
        public String mac;

    public Message() {
    }

    public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }
    }
