/*
 * Copyright 2017 Guilherme Paulino
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulino.secrettalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.paulino.secrettalk.logger.Log;
import com.paulino.secrettalk.logger.LogListActivity;

/**
 * This is an App Compat Activity, which implements the class FragmentActivity.
 * It allows the BluetoothChatFragment to create the layout View described in the class definition
 * (dynamically), instead of being defined in a XML file.
 */
public class MessageActivity extends AppCompatActivity {

    public static final String TAG = "BlueChat";

    private String mKeyword;                // Keyword used for cryptography
    private boolean discoverable = false;   // Turn bluetooth discoverable
    // This fragment controls Bluetooth to communicate with other devices
    private BluetoothChatFragment fragment1 = new BluetoothChatFragment();

    /**
     * Navigation View listener
     */
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // See the keyword
                    BlueChatPress();
                    return true;
                case R.id.navigation_dashboard:
                    // Open Log Cat list activity
                    LogCatPress();
                    return true;
                case R.id.connect_devices:
                    // Connect to remote devices over Bluetooth
                    if (!discoverable) {
                        // Makes this device discoverable for 300 seconds (5 minutes).
                        fragment1.ensureDiscoverable();
                        discoverable = true;
                        Toast toast = Toast.makeText(MessageActivity.this, "Discoverable", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    } else {
                        // Launch the Device list activity to see paired devices and scan for new ones
                        try {
                            fragment1.insecureConnect();
                        } catch (Exception e) {
                            Toast toast = Toast.makeText(MessageActivity.this, "Something got wrong.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                            toast.show();
                            Log.e(TAG, "Something got wrong: Could not connect to a device.");
                        }
                    }
                    return true;
            }
            return false;
        }
    };

    /**
     * Shows the defined keyword used
     */
    private void BlueChatPress() {
        String toastText;
        if (!mKeyword.isEmpty()) {
            toastText = "Key: " + mKeyword;
        } else {
            toastText = "Empty keyword";
        }
        Toast toast = Toast.makeText(MessageActivity.this, toastText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    /**
     * Calls Log Cat list for debugging
     */
    private void LogCatPress() {
        Intent serverIntent2 = new Intent(this, LogListActivity.class);
        startActivity(serverIntent2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Receives keyword string from Main Activity
        Bundle b = getIntent().getExtras();
        mKeyword = b.getString("Keyword", "");

        // Initializes Bottom Navigation view and its listener
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            // Sends keyword string to BluetoothChatFragment
            Bundle c = new Bundle();
            c.putString("mKeyword", mKeyword);
            fragment1.setArguments(c);
            // Calls Bluetooth Chat Fragment content Views
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_content, fragment1);
            transaction.commit();
        }
    }

    /**
     * Ensure any created loaders are now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Enter BlueChat");
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key.  The default implementation simply finishes the current activity.
     */
    @Override
    public void onBackPressed() {
        Log.d(TAG, "Leave BlueChat");
        super.onBackPressed();
    }
}
