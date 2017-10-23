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

package com.paulino.secrettalk.logger;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.ScrollView;

import com.paulino.secrettalk.MainActivity;
import com.paulino.secrettalk.R;

/**
 * Log Cat list activity
 */
public class LogListActivity extends Activity {

    public ScrollView mScrollView;
    public LogView mLogView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable extended window features
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.fragment_log_cat);

        mScrollView = (ScrollView) findViewById(R.id.logview_scroll);
        mLogView = (LogView) findViewById(R.id.logview_logger);

        // Setup the scroll bars to fit the list, and focus on most recent
        mLogView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        // After creates the activity, the application must bind the Log View
        // with the ArrayList<String> LogList content
        MainActivity.msgFilter.setNext(mLogView);
    }
}
