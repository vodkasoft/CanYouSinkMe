/**
 * Vodkasoft(R)
 * Created by jomarin on 4/2/14.
 */

package com.vodkasoft.canyousinkme.connectivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.vodkasoft.canyousinkme.game.R;
import com.vodkasoft.canyousinkme.utils.JsonSerializer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BleutoothManager {

    private static final int DEFAULT_DISCOVERABLE_DURATION = 300;
    private static final String MESSAGE_KEY = "CanYouSinkMe?";
    private static final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When a device is found
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                discoverableDevicesName.add(device.getName());
                discoverableDevicesAddress.add(device.getAddress());

            }
        }
    };
    private static String STATE = "NULL";
    private static ArrayList<String> discoverableDevicesAddress = new ArrayList<String>();
    private static ArrayList<String> discoverableDevicesName = new ArrayList<String>();
    private static AcceptThread mAcceptThread = null;
    private static Activity mActivity;
    private static BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private static ConnectThread mConnectThread = null;
    private static ConnectedThread mConnectedThread = null;
    private static Handler mHandler = new Handler() {
        @Override
        synchronized public void handleMessage(Message pMessage) {
            Bundle bundle = pMessage.getData();
            String message = bundle.getString(MESSAGE_KEY);
            BluetoothMessage btMessage = (BluetoothMessage) JsonSerializer.fromJsonToObject(
                    message, BluetoothMessage.class);
            mMessageQueue.add(btMessage);
            return;
        }
    };
    private static Queue<BluetoothMessage> mMessageQueue = new LinkedList<BluetoothMessage>();

    public static String getState(){
        return STATE;
    }
    public static void setSTATE(String pState){
        STATE = pState;
    }
    public static void setActivity(Activity pActivity){
        mActivity = pActivity;
    }
    /**
     * Checks for device status (on, off, not available)
     *
     * @return String with device status
     */
    public static String checkStatus() {
        if (mAdapter == null)
            return mActivity.getResources().getString(R.string.no_bluetooth_available);
        else if (mAdapter.isEnabled())
            return mActivity.getResources().getString(R.string.bluetooth_enabled);
        else
            return mActivity.getResources().getString(R.string.bluetooth_not_enabled);
    }

    public static void unregisterReceiver(){
        mActivity.unregisterReceiver(mReceiver);
    }

    /**
     * Cancels connectivity threads, unregisters receivers and cancels discovery mode. Use in onDestroy()
     * method
     */
    public static void cleanUp() {
        if (mReceiver == null) {
            mActivity.unregisterReceiver(mReceiver);
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
        }
        mAdapter.cancelDiscovery();
    }

    /**
     * Turns bluetooth on, shows a pop-up
     */
    public static void enableBluetooth() {
        int REQUEST_ENABLE_BT = 1;
        if (!mAdapter.isEnabled()) {
            Intent enableIntent = new Intent(mAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Starts Bluetooth's discovery mode
     */
    public static void findDevices() {
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mActivity.registerReceiver(mReceiver, filter);
        // Clear current device lists
        discoverableDevicesAddress.clear();
        discoverableDevicesName.clear();
        // Start Bluetooth scanning
        mAdapter.startDiscovery();
    }

    /**
     * Returns BluetoothAdapter from device
     *
     * @return Device's BluetoothAdapter
     */
    public static BluetoothAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Returns discoverable devices address
     *
     * @return ArrayList<String> with discoverable devices
     */
    public static ArrayList<String> getDiscoverableDevicesAddress() {
        return discoverableDevicesAddress;
    }

    /**
     * Returns discoverable devices
     *
     * @return ArrayList<String> with discoverable devices
     */
    public static ArrayList<String> getDiscoverableDevicesName() {
        return discoverableDevicesName;
    }

    /**
     * Returns paired devices
     *
     * @return ArrayList with pairedDevices
     */
    public static ArrayList<String> getPairedDevices() {
        ArrayList<String> bondedDevices = new ArrayList<String>();
        Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();

        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                bondedDevices.add(device.getAddress());
            }

        }
        return bondedDevices;
    }

    /**
     * Saves the connection thread and initializes the connection member
     *
     * @param pConnectedThread used to initialize ConnectedThread method
     */
    public static void holdConnectThread(ConnectedThread pConnectedThread) {
        mConnectedThread = pConnectedThread;
    }

    /**
     * Checks if connection is active or if it was stablished
     *
     * @return true if connection is currently active, false if the is no connection thread
     */
    public static boolean isConnectionActive() {
        return mConnectedThread != null ? true : false;
    }

    /**
     * Starts discoverable mode on device for EXTRA_DISCOVERABLE_DURATION (time constant)
     */
    public static void setDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
                DEFAULT_DISCOVERABLE_DURATION);
        mActivity.startActivity(discoverableIntent);
    }

    /**
     * Looks for stablished connection and client threads, cancels them and starts a new client
     * thread
     *
     * @param pDevice
     * @param pUUID
     */
    public static void startClientConnection(BluetoothDevice pDevice, String pUUID) {
        // Cancel any thread trying to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread with an active connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start a thread to listen on a BluetoothServerSocket
        if (mConnectThread == null) {
            mConnectThread = new ConnectThread(pDevice, mHandler, mActivity.getApplicationContext(), pUUID);
            mConnectThread.start();
        }
    }

    /**
     * Looks for stablished connections and sever threads and shuts them down. Initializes a new
     * acceptance threads
     *
     * @param pUUID
     */
    public static void startServerConnection(String pUUID) {
        // Cancel any thread trying to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread with an active connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start a thread to listen for new connections
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread(mHandler, mActivity.getApplicationContext(), pUUID);
            mAcceptThread.start();
        }
    }

    public static boolean messageQueueIsEmpty(){
        return mMessageQueue.isEmpty() ? true : false;
    }

    public static BluetoothMessage dequeueMessage(){
        if(!mMessageQueue.isEmpty())
            return mMessageQueue.poll();
        else return null;
    }

    public static void sendMessage(int key, String data){
        BluetoothMessage btMessage = new BluetoothMessage(key, data);
        String jsonMessage = JsonSerializer.fromObjectToJson(btMessage);
        mConnectedThread.write(jsonMessage.getBytes());
    }

}