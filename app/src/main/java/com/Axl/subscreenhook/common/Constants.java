package com.Axl.subscreenhook.common;

public final class Constants {
    public static final String MODULE_PACKAGE = "com.Axl.subscreenhook";
    public static final String TARGET_PACKAGE = "com.xiaomi.subscreencenter";
    public static final String MAIN_PANEL_CLASS = "com.xiaomi.subscreencenter.MainPanel";
    public static final String PREF_GROUP = "module_config";
    public static final String ACTION_SETTING_CHANGED = "com.backscreen.helper.ACTION_SETTING_CHANGED";
    public static final String EXTRA_SETTING_KEY = "setting_key";
    public static final String EXTRA_SETTING_VALUE = "setting_value";
    public static final String KEY_DISABLE_LONG_PRESS_EDIT = "disable_long_press_edit";
    public static final String KEY_FLOATING_NAV_BAR = "floating_nav_bar";

    // Z1.t: old gesture class
    public static final String HOOK_CLASS = "Z1.t";
    // Z1.v: new gesture class
    public static final String HOOK_CLASS_LONG_PRESS_NEW = "Z1.v";

    public static final String HOOK_METHOD_GATE = "e";
    public static final String HOOK_METHOD_GATE_NEW = "g";
    public static final String HOOK_METHOD_LONG_PRESS_TOUCH_NEW = "f";
    public static final String HOOK_METHOD_RUN = "run";

    public static final String LOG_TAG = "RearScreenLongPressToggle";

    private Constants() {
    }
}
