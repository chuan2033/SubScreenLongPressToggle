package com.Axl.subscreenhook.bridge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.Axl.subscreenhook.app.ModuleApp;
import com.Axl.subscreenhook.common.Constants;
import com.Axl.subscreenhook.hook.ModuleMain;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.service.XposedService;

public final class PrefsBridge {
    private static final String TAG = Constants.LOG_TAG + ":PrefsBridge";
    public static final boolean DEFAULT_DISABLE_LONG_PRESS_EDIT = true;
    private static volatile Context hookContext;

    private PrefsBridge() {
    }

    @NonNull
    private static SharedPreferences local(@NonNull Context context) {
        return context.getSharedPreferences(Constants.PREF_GROUP, Context.MODE_PRIVATE);
    }

    public static void setHookContext(@Nullable Context context) {
        if (context != null) {
            hookContext = context.getApplicationContext();
        }
    }

    @Nullable
    private static SharedPreferences localForHook() {
        Context context = hookContext;
        if (context == null) {
            return null;
        }
        try {
            return local(context);
        } catch (Throwable e) {
            Log.w(TAG, "Failed to get local prefs for hook", e);
            return null;
        }
    }

    @Nullable
    private static SharedPreferences remote() {
        try {
            XposedService service = ModuleApp.getService();
            if (service == null) {
                return null;
            }
            return service.getRemotePreferences(Constants.PREF_GROUP);
        } catch (Throwable e) {
            Log.w(TAG, "Failed to get remote prefs", e);
            return null;
        }
    }

    @Nullable
    private static SharedPreferences remoteForHook() {
        ModuleMain module = ModuleMain.getRunningInstance();
        if (module == null) {
            return null;
        }
        try {
            return module.getRemotePreferences(Constants.PREF_GROUP);
        } catch (Throwable e) {
            Log.w(TAG, "Failed to get remote prefs for hook", e);
            return null;
        }
    }

    public static boolean readDisableLongPressForUi(@NonNull Context context) {
        SharedPreferences remote = remote();
        if (remote != null) {
            boolean value = remote.getBoolean(
                    Constants.KEY_DISABLE_LONG_PRESS_EDIT,
                    DEFAULT_DISABLE_LONG_PRESS_EDIT
            );
            local(context).edit()
                    .putBoolean(Constants.KEY_DISABLE_LONG_PRESS_EDIT, value)
                    .apply();
            return value;
        }
        return local(context).getBoolean(
                Constants.KEY_DISABLE_LONG_PRESS_EDIT,
                DEFAULT_DISABLE_LONG_PRESS_EDIT
        );
    }

    public static void writeDisableLongPressFromUi(@NonNull Context context, boolean disabled) {
        local(context).edit()
                .putBoolean(Constants.KEY_DISABLE_LONG_PRESS_EDIT, disabled)
                .apply();
        SharedPreferences remote = remote();
        if (remote != null) {
            remote.edit()
                    .putBoolean(Constants.KEY_DISABLE_LONG_PRESS_EDIT, disabled)
                    .apply();
        }
    }

    public static void syncOnServiceAvailable(@NonNull Context context, @NonNull XposedService service) {
        try {
            SharedPreferences localPrefs = local(context);
            SharedPreferences remotePrefs = service.getRemotePreferences(Constants.PREF_GROUP);
            String key = Constants.KEY_DISABLE_LONG_PRESS_EDIT;
            if (remotePrefs.contains(key)) {
                localPrefs.edit()
                        .putBoolean(key, remotePrefs.getBoolean(key, DEFAULT_DISABLE_LONG_PRESS_EDIT))
                        .apply();
            } else if (localPrefs.contains(key)) {
                remotePrefs.edit()
                        .putBoolean(key, localPrefs.getBoolean(key, DEFAULT_DISABLE_LONG_PRESS_EDIT))
                        .apply();
            } else {
                remotePrefs.edit().putBoolean(key, DEFAULT_DISABLE_LONG_PRESS_EDIT).apply();
                localPrefs.edit().putBoolean(key, DEFAULT_DISABLE_LONG_PRESS_EDIT).apply();
            }
        } catch (Throwable e) {
            Log.w(TAG, "Failed to sync prefs on service available", e);
        }
    }

    public static boolean shouldBlockLongPressEdit(@NonNull XposedModule module) {
        try {
            SharedPreferences localPrefs = localForHook();
            if (localPrefs != null) {
                return localPrefs.getBoolean(
                        Constants.KEY_DISABLE_LONG_PRESS_EDIT,
                        DEFAULT_DISABLE_LONG_PRESS_EDIT
                );
            }
            SharedPreferences remotePrefs = module.getRemotePreferences(Constants.PREF_GROUP);
            if (remotePrefs != null) {
                return remotePrefs.getBoolean(
                        Constants.KEY_DISABLE_LONG_PRESS_EDIT,
                        DEFAULT_DISABLE_LONG_PRESS_EDIT
                );
            }
            return DEFAULT_DISABLE_LONG_PRESS_EDIT;
        } catch (Throwable e) {
            Log.w(TAG, "Failed to read shouldBlockLongPressEdit", e);
            return DEFAULT_DISABLE_LONG_PRESS_EDIT;
        }
    }

    public static void applyIncomingSettingForUi(@NonNull Context context, @NonNull String key, boolean value) {
        try {
            local(context).edit().putBoolean(key, value).apply();
            SharedPreferences remotePrefs = remote();
            if (remotePrefs != null) {
                remotePrefs.edit().putBoolean(key, value).apply();
            }
        } catch (Throwable e) {
            Log.w(TAG, "Failed to apply incoming setting for UI: " + key, e);
        }
    }

    public static void applyIncomingSettingForHook(@NonNull String key, boolean value) {
        try {
            SharedPreferences localPrefs = localForHook();
            if (localPrefs != null) {
                localPrefs.edit().putBoolean(key, value).apply();
            }
            SharedPreferences remotePrefs = remoteForHook();
            if (remotePrefs != null) {
                remotePrefs.edit().putBoolean(key, value).apply();
            }
        } catch (Throwable e) {
            Log.w(TAG, "Failed to apply incoming setting for hook: " + key, e);
        }
    }

    public static boolean readFloatingNavBar(@NonNull Context context) {
        return local(context).getBoolean(Constants.KEY_FLOATING_NAV_BAR, false);
    }

    public static void writeFloatingNavBar(@NonNull Context context, boolean floating) {
        local(context).edit().putBoolean(Constants.KEY_FLOATING_NAV_BAR, floating).apply();
    }
}
