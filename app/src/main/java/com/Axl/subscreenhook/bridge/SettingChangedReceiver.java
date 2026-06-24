package com.Axl.subscreenhook.bridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.Axl.subscreenhook.common.Constants;

public class SettingChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        if (!Constants.ACTION_SETTING_CHANGED.equals(intent.getAction())) {
            return;
        }
        String key = intent.getStringExtra(Constants.EXTRA_SETTING_KEY);
        if (key == null) {
            return;
        }
        if (intent.hasExtra(Constants.EXTRA_SETTING_VALUE)) {
            boolean value = intent.getBooleanExtra(Constants.EXTRA_SETTING_VALUE, false);
            PrefsBridge.applyIncomingSettingForUi(context, key, value);
        }
    }
}
