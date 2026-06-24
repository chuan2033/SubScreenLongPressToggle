package com.Axl.subscreenhook.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.Axl.subscreenhook.app.ModuleApp
import com.Axl.subscreenhook.bridge.PrefsBridge
import com.Axl.subscreenhook.bridge.SettingsSyncBridge
import com.Axl.subscreenhook.common.Constants
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.darkColorScheme
import top.yukonga.miuix.kmp.theme.lightColorScheme

@Composable
internal fun RearScreenApp() {
    val context = LocalContext.current
    val serviceAvailable = ModuleApp.getService() != null

    var disableLongPress by remember {
        mutableStateOf(PrefsBridge.readDisableLongPressForUi(context))
    }
    var floatingNavBar by remember {
        mutableStateOf(PrefsBridge.readFloatingNavBar(context))
    }

    MiuixTheme(
        colors = lightColorScheme()
    ) {
        HomeScreen(
            disableLongPress = disableLongPress,
            serviceAvailable = serviceAvailable,
            floatingNavBar = floatingNavBar,
            onDisableLongPressChange = { newValue ->
                disableLongPress = newValue
                PrefsBridge.writeDisableLongPressFromUi(context, newValue)
                SettingsSyncBridge.sendBooleanSetting(context, Constants.KEY_DISABLE_LONG_PRESS_EDIT, newValue)
            },
            onFloatingNavBarChange = { newValue ->
                floatingNavBar = newValue
                PrefsBridge.writeFloatingNavBar(context, newValue)
            }
        )
    }
}
