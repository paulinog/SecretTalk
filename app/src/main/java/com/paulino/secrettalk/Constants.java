package com.paulino.secrettalk;

/**
 * Created by Guilherme Paulino on 9/30/2017.
 */

//Defines several constants used between {@link BluetoothChatService} and the UI.
public interface Constants {
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name1";
    public static final String TOAST = "toast";

}
