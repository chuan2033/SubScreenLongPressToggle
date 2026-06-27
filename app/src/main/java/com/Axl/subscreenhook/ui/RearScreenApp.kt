package com.Axl.subscreenhook.ui

import android.app.Activity
import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsControllerCompat
import com.Axl.subscreenhook.app.ModuleApp
import com.Axl.subscreenhook.bridge.PrefsBridge
import com.Axl.subscreenhook.bridge.SettingsSyncBridge
import com.Axl.subscreenhook.common.Constants
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.darkColorScheme
import top.yukonga.miuix.kmp.theme.lightColorScheme

@Composable
internal fun RearScreenApp() {
    val context = LocalContext.current
    var serviceAvailable by remember { mutableStateOf(ModuleApp.getService() != null) }

    LaunchedEffect(Unit) {
        while (true) {
            val available = ModuleApp.getService() != null
            if (available != serviceAvailable) {
                serviceAvailable = available
            }
            delay(500)
        }
    }

    var disableLongPress by remember {
        mutableStateOf(PrefsBridge.readDisableLongPressForUi(context))
    }
    var removeWallpaperLimit by remember {
        mutableStateOf(PrefsBridge.readRemoveWallpaperLimitForUi(context))
    }
    var floatingNavBar by remember {
        mutableStateOf(PrefsBridge.readFloatingNavBar(context))
    }

    val isDark = false

    DisposableEffect(isDark) {
        val activity = context as? ComponentActivity
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ) { isDark },
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ) { isDark },
        )
        activity?.window?.isNavigationBarContrastEnforced = false
        onDispose {}
    }

    LaunchedEffect(isDark) {
        val activity = context as? ComponentActivity ?: return@LaunchedEffect
        WindowInsetsControllerCompat(activity.window, activity.window.decorView).apply {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }
    }

    MiuixTheme(
        colors = lightColorScheme()
    ) {
        HomeScreen(
            disableLongPress = disableLongPress,
            removeWallpaperLimit = removeWallpaperLimit,
            serviceAvailable = serviceAvailable,
            floatingNavBar = floatingNavBar,
            onDisableLongPressChange = { newValue ->
                disableLongPress = newValue
                PrefsBridge.writeDisableLongPressFromUi(context, newValue)
                SettingsSyncBridge.sendBooleanSetting(context, Constants.KEY_DISABLE_LONG_PRESS_EDIT, newValue)
            },
            onRemoveWallpaperLimitChange = { newValue ->
                removeWallpaperLimit = newValue
                PrefsBridge.writeRemoveWallpaperLimitFromUi(context, newValue)
                SettingsSyncBridge.sendBooleanSetting(context, Constants.KEY_REMOVE_WALLPAPER_LIMIT, newValue)
            },
            onFloatingNavBarChange = { newValue ->
                floatingNavBar = newValue
                PrefsBridge.writeFloatingNavBar(context, newValue)
            }
        )
    }
}
