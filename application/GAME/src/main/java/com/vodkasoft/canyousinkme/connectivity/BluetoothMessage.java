package com.vodkasoft.canyousinkme.connectivity;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class BluetoothMessage {
    private String data;
    private String key;

    public BluetoothMessage(){
        // No args constructor for serialization
    }

    public BluetoothMessage(String key, String data) {
        this.data = data;
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
