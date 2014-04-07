/**
 * Vodkasoft(R)
 * Created by jomarin on 4/2/14.
 */

package com.vodkasoft.canyousinkme.connectivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {

    private final BluetoothDevice mmDevice;
    private final BluetoothSocket mmSocket;
    private Handler mHandler;

    /**
     * Initializes client thread, starts listening socket
     *
     * @param pDevice           BluetoothDevice
     * @param pHandler          Connection handler
     * @param pContext          Application context
     */
    public ConnectThread(BluetoothDevice pDevice, Handler pHandler, Context pContext, String pUUID) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        mHandler = pHandler;
        BluetoothSocket tmp = null;
        mmDevice = pDevice;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = pDevice.createRfcommSocketToServiceRecord(
                    UUID.fromString(pUUID));
        } catch (IOException e) {
        }
        mmSocket = tmp;
    }

    /**
     * Will cancel an in-progress connection, and close the socket
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }

    /**
     * Thread loop, looks for available server
     */
    public void run() {
        // Cancel discovery because it will slow down the connection
        BleutoothManager.getAdapter().cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) {
            }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        ConnectedThread mConnectedThread = new ConnectedThread(mmSocket, mHandler);
        mConnectedThread.start();
        BleutoothManager.holdConnectThread(mConnectedThread);
    }
}
