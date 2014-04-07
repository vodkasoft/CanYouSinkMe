/**
 * Vodkasoft(R)
 * Created by jomarin
 */

package com.vodkasoft.canyousinkme.connectivity;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {

    private final BluetoothServerSocket mmServerSocket;
    private Handler mHandler;

    /**
     * Initializes thread socket and other members
     *
     * @param pHandler          Application handler
     * @param pContext          Application context
     */
    public AcceptThread(Handler pHandler, Context pContext,
                        String pUUID) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        mHandler = pHandler;
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = BleutoothManager.getAdapter().listenUsingRfcommWithServiceRecord(
                    "Server", UUID.fromString(pUUID));
        } catch (IOException e) {
        }
        mmServerSocket = tmp;
    }

    /**
     * Will cancel the listening socket, and cause the thread to finish
     */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
        }
    }

    /**
     * Thread loop, listens for client connection
     */
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                break;
            }
            // If a connection was accepted
            if (socket != null) {
                // Do work to manage the connection (in a separate thread)
                ConnectedThread mConnectedThread = new ConnectedThread(socket, mHandler);
                mConnectedThread.start();
                BleutoothManager.holdConnectThread(mConnectedThread);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
