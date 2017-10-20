package com.paulino.secrettalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.paulino.secrettalk.logger.Log;
import com.paulino.secrettalk.logger.LogListActivity;

/**
 * Created by Guilherme Paulino on 10/1/2017.
 */

public class MessageActivity extends AppCompatActivity {
    public static final String TAG = "BlueChat";
    private String mKeyword;
    private boolean discoverable = false;
    public FragmentTransaction transaction;
    public BluetoothChatFragment fragment1 = new BluetoothChatFragment();

    private int counter =1;
    private static String msg117, msg119;
    private static String name = "Guilherme";
    private byte[] encrypted_name;

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    BlueChatPress();
                    return true;
                case R.id.navigation_dashboard:
                    LogCatPress();
                    return true;
                case R.id.connect_devices:
                    if (!discoverable) {
                        fragment1.ensureDiscoverable();
                        discoverable = true;
                        Toast.makeText(MessageActivity.this, "Discoverable", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            fragment1.insecureConnect();
                        } catch (Exception e) {
                            Toast.makeText(MessageActivity.this, "Something got wrong.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Something got wrong: Could not connect to a device.");
                        }
                    }
                    return true;
            }
            return false;
        }
    };

    private void BlueChatPress() {
//        if(counter == 1) {
//            encrypted_name = AdvancedEncryptionStandard.encrypt(name, mKeyword);
//            msg117 = new String(encrypted_name, 0, name.length());
//            Toast toast = Toast.makeText(MessageActivity.this, ("Encrypted: " + msg117), Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
//            counter++;
//        }
//        else if(counter == 2) {
//            msg119 = AdvancedEncryptionStandard.decrypt(encrypted_name, mKeyword, name.length());
//            Toast toast = Toast.makeText(MessageActivity.this, "Decrypt: "+msg119, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
//            toast.show();
//
//            counter++;
//        }
//        else {
            String toastText;
            if (!mKeyword.isEmpty()) {
                toastText = "Key: " + mKeyword;

                Toast toast1 = Toast.makeText(MessageActivity.this, toastText, Toast.LENGTH_LONG);
                toast1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast1.show();
                Toast toast2 = Toast.makeText(MessageActivity.this, toastText, Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast2.show();
            } else {
                toastText = "Empty keyword";
                Toast toast1 = Toast.makeText(MessageActivity.this, toastText, Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                toast1.show();
            }
//        }
    }

    private void LogCatPress() {
        Intent serverIntent2 = new Intent(this, LogListActivity.class);
        startActivity(serverIntent2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_content, fragment1);
            transaction.commit();
        }

        Bundle b = getIntent().getExtras();
        mKeyword = b.getString("Keyword", "");

        Bundle c = new Bundle();
        c.putString("mKeyword", mKeyword);
        fragment1.setArguments(c);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Enter BlueChat");
    }
}
