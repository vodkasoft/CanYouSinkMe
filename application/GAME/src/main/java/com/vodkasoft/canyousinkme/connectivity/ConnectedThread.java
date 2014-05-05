/**
 * Vodkasoft(R)
 * Created by jomarin on 4/2/14.
 */

package com.vodkasoft.canyousinkme.connectivity;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private final String MESSAGE_KEY = "CanYouSinkMe?";
    private final int BUFFER_SIZE = 1024;
    private final String CHAR_BEFORE_GARBAGE = "}";
    private final int MESSAGE_SUBSTRING_START_INDEX = 0;
    private final int EXCLUSIVE_INDEX_FIXER = 1;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final BluetoothSocket mmSocket;
    private Handler mHandler;

    /**
     * Initializes thread socket and i/o strems
     *
     * @param pSocket  Stablished connection socket
     * @param pHandler Connection handler
     */
    public ConnectedThread(BluetoothSocket pSocket, Handler pHandler) {
        mHandler = pHandler;
        mmSocket = pSocket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = pSocket.getInputStream();
            tmpOut = pSocket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    /**
     * Call this from the main activity to shutdown the connection
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }

    /**
     * Thread loop, keeps connection alive, sends and receives bytes
     */
    public void run() {
        byte[] buffer;

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            buffer = new byte[BUFFER_SIZE];
            try {
                // Read from the InputStream
                mmInStream.read(buffer);
                String messageData = new String(buffer);
                // Clean garbage
                int garbageIndex = messageData.lastIndexOf(CHAR_BEFORE_GARBAGE) + EXCLUSIVE_INDEX_FIXER;
                messageData = messageData.substring(MESSAGE_SUBSTRING_START_INDEX, garbageIndex);
                // Send the obtained bytes to the UI activity
                Message message = Message.obtain(mHandler);
                Bundle mBundle = new Bundle();
                mBundle.putString(MESSAGE_KEY, messageData);
                message.setData(mBundle);
                mHandler.sendMessage(message);
            } catch (Exception e) {
                break;
            }
        }
    }

    /**
     * Call this from the main activity to send data to the remote device
     */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }
}
