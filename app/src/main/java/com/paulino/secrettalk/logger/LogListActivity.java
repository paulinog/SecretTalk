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
 * Created by Guilherme Paulino on 10/16/2017.
 */

public class LogListActivity extends Activity {

    public static final String TAG = "LogCat";

    public ScrollView mScrollView;

    public LogView mLogView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.fragment_log_cat);

        mScrollView = (ScrollView) findViewById(R.id.logview_scroll);
        mLogView = (LogView) findViewById(R.id.logview_logger);

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

        MainActivity.msgFilter.setNext(mLogView);
    }
}
