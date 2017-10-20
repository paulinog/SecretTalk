package com.paulino.secrettalk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Guilherme Paulino on 9/30/2017.
 */

public class BluechatReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent toPretend = new Intent(context, MessageActivity.class);

        toPretend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(toPretend);
    }
}
