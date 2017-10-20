/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paulino.secrettalk.logger;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ScrollView;

import com.paulino.secrettalk.R;

/**
 * Simple fragment which contains a LogView and uses is to output log data it receives
 * through the LogNode interface.
 */
public class LogFragment extends Fragment {

    private LogView mLogView;
    private ScrollView mScrollView;

    public LogFragment() {}

//    public View inflateViews() {
//        mScrollView = new ScrollView(getActivity());
//        ViewGroup.LayoutParams scrollParams = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//        mScrollView.setLayoutParams(scrollParams);
//
//        mLogView = new LogView(getActivity());
//        ViewGroup.LayoutParams logParams = new ViewGroup.LayoutParams(scrollParams);
//        logParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        mLogView.setLayoutParams(logParams);
//        mLogView.setClickable(true);
//        mLogView.setFocusable(true);
//        mLogView.setTypeface(Typeface.MONOSPACE);
//
//        // Want to set padding as 16 dips, setPadding takes pixels.  Hooray math!
//        int paddingDips = 16;
//        double scale = getResources().getDisplayMetrics().density;
//        int paddingPixels = (int) ((paddingDips * (scale)) + .5);
//        mLogView.setPadding(paddingPixels, paddingPixels, paddingPixels, paddingPixels);
//        mLogView.setCompoundDrawablePadding(paddingPixels);
//
//        mLogView.setGravity(Gravity.BOTTOM);
//        mLogView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Holo_Medium);
//
//        mScrollView.addView(mLogView);
//        return mScrollView;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        View result = inflateViews();

//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); // -> copied from the device list

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
//        return result;

        return inflater.inflate(R.layout.fragment_log_cat, container, false);
    }

//    private final boolean requestWindowFeature(int featureId) {
//        return getWindow().requestFeature(featureId);
//    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mScrollView = (ScrollView) view.findViewById(R.id.logview_scroll);
        mLogView = (LogView) view.findViewById(R.id.logview_logger);

        mScrollView.addView(mLogView);

        super.onViewCreated(view, savedInstanceState);
    }

    public LogView getLogView() {
        return mLogView;
    }
}