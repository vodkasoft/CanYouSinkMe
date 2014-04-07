package com.vodkasoft.canyousinkme.connectivity;

/**
 * Vodkasoft (R)
 * Created by jomarin on 4/6/14.
 */
public class BluetoothMessage {
    private String data;
    private int key;

    public BluetoothMessage(){
        // No args constructor for serialization
    }

    public BluetoothMessage(int key, String data) {
        this.data = data;
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
