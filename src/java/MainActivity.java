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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paulino.secrettalk.logger.Log;
import com.paulino.secrettalk.logger.LogWrapper;
import com.paulino.secrettalk.logger.MessageOnlyLogFilter;

import java.util.ArrayList;

/**
 * The launcher activity, containing an Edit Text field in password format and a Button to Sign in
 * the BlueChat application. It verifies the size of the Keyword defined, so it must be compatible
 * with the 128/192/256-bits AES cryptography algorithm for encoded strings of the text messages.
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "SecretTalk";

    // Log Cat array list
    public static ArrayList<String> LogList = new ArrayList<String>();

    // Logger interfaces
    public LogWrapper logWrapper;
    // Prints data out to the console using Android's native log mechanism
    public static MessageOnlyLogFilter msgFilter;
    // Filter strips out everything except the message text; Adds in LogList

    private UserLoginTask mAuthTask = null;
    private EditText mKeywordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView countChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // Receives the keyword string inserted on Edit Text view
        mKeywordView = (EditText) findViewById(R.id.keyword);

        // Pressing Enter Secret button should attempt to sign in
        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignIn();
            }
        });

        // Login form is a Scroll View
        // It has an Edit Text and a Button
        mLoginFormView = findViewById(R.id.login_form);
        // Progress View allows to apply an animation of a progress bar/circle
        // in case the sign in attempt would need some time to process
        mProgressView = findViewById(R.id.login_progress);
        // This Text View shows how many characters were already typed
        countChar = (TextView) findViewById(R.id.count_char);

        // This listener counts the typed characters for the AES keyword
        mKeywordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 1) { // plural char phrase
                    countChar.setText(String.valueOf(s.length())+" characters");
                } else if(s.length() == 1) { // single char
                    countChar.setText(String.valueOf(s.length())+" character");
                } else { // empty keyword
                    countChar.setText(R.string.wo_cryptography);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Prints data out to the console using Android's native log mechanism
        logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter); // Sets the LogNode data will be sent to...

        Log.i(TAG, "Start Application");
    }

    /**
     * Keyword validation to AES format compatibility
     */
    private void attemptSignIn() {
        if (mAuthTask != null) {
            return;
        }
        mKeywordView.setError(null);
        String keyword = mKeywordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // in case the user decides to put a keyword, it must be valid
        if (!TextUtils.isEmpty(keyword) && !isKeywordValid(keyword)) {
            mKeywordView.setError(getString(R.string.error_invalid_keyword));
            focusView = mKeywordView;
            cancel = true;
        }

        if (cancel) {
            // if it was an invalid key, the Edit Text view will request the cursor focus
            focusView.requestFocus();
        } else {
            // if a key was valid, shows a progress bar and executes the access to BlueChat
            showProgress(true);
            mAuthTask = new UserLoginTask(keyword);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * A keyword is valid only in the following conditions
     *
     * @param keyword User input of word-phrase to be used to encrypt messages
     * @return Condition if entry is valid
     */
    private boolean isKeywordValid(String keyword) {
        // AES cryptography algorithm must be 128, 192 or 256-bits.
        // It request 16, 24 or 32 characters to keyword
        return ( keyword.isEmpty() || keyword.length() == 16 || keyword.length() == 24 || keyword.length() == 32 );
    }

    /**
     * Progress bar animation
     *
     * @param show Enables the progress bar to be visible
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // 200 milliseconds
            int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // If the progress view API is not available,
            // show and hide simple UI components
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * AsyncTask enables proper and easy use of the UI thread. This class allows
     * to perform background operations and publish results on the UI thread without
     * having to manipulate threads and/or handlers.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        // receives the encrypt keyword
        private final String mKeyword;
        UserLoginTask(String keyword) {
            mKeyword = keyword;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                // Welcome message
                Toast toastText = Toast.makeText(MainActivity.this, "Welcome to BlueChat",Toast.LENGTH_SHORT);
                toastText.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
                toastText.show();

                // Starts the BlueChat activity as a new Task
                Intent toPretend = new Intent(MainActivity.this, MessageActivity.class);
                toPretend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Send the inserted key by user input
                toPretend.putExtra("Keyword", mKeyword);
                if (mKeyword.isEmpty()) {
                    Log.i(TAG, "Empty keyword");
                } else {
                    Log.i(TAG, "Keyword: "+mKeyword);
                }
                startActivity(toPretend);

            } else {
                // If the keyword is invalid
                mKeywordView.setError(getString(R.string.error_incorrect_keyword));
                mKeywordView.requestFocus();
            }
        }

        // In case it is canceled
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

