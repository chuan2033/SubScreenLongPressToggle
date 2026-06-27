package com.Axl.subscreenhook.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Axl.subscreenhook.BuildConfig
import com.Axl.subscreenhook.ui.components.CardBlock
import com.Axl.subscreenhook.ui.components.SettingsInfoRow
import com.Axl.subscreenhook.ui.components.StatusCard
import com.Axl.subscreenhook.ui.theme.HomeUiTokens
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.utils.PressFeedbackType

@Composable
internal fun HomePage(
    disableLongPress: Boolean,
    removeWallpaperLimit: Boolean,
    floatingNavBar: Boolean,
    serviceAvailable: Boolean,
    onDisableLongPressChange: (Boolean) -> Unit,
    onRemoveWallpaperLimitChange: (Boolean) -> Unit,
    onFloatingNavBarChange: (Boolean) -> Unit
) {
    StatusCard(
        serviceAvailable = serviceAvailable,
        moduleVersion = BuildConfig.VERSION_NAME
    )

    SmallTitle(text = "功能设置", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock(pressFeedbackType = PressFeedbackType.None) {
        SwitchPreference(
            checked = disableLongPress,
            onCheckedChange = onDisableLongPressChange,
            title = "禁用背屏长按切换壁纸",
            summary = if (disableLongPress) {
                "当前会拦截长按进入编辑态"
            } else {
                "当前恢复原厂长按行为"
            }
        )
        SwitchPreference(
            checked = removeWallpaperLimit,
            onCheckedChange = onRemoveWallpaperLimitChange,
            title = "去除背屏壁纸数量限制",
            summary = if (removeWallpaperLimit) {
                "已去除15张壁纸上限"
            } else {
                "当前保持默认15张上限"
            }
        )
    }

    SmallTitle(text = "导航栏", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock(pressFeedbackType = PressFeedbackType.None) {
        SwitchPreference(
            checked = floatingNavBar,
            onCheckedChange = onFloatingNavBarChange,
            title = "悬浮底栏",
            summary = if (floatingNavBar) "当前使用悬浮底栏" else "当前使用普通底栏"
        )
    }

    SmallTitle(text = "当前配置", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock {
        SettingsInfoRow("长按禁用", if (disableLongPress) "开启" else "关闭")
        SettingsInfoRow("壁纸限制", if (removeWallpaperLimit) "已去除" else "保持默认")
        SettingsInfoRow("底栏样式", if (floatingNavBar) "悬浮底栏" else "普通底栏")
    }

    Spacer(modifier = Modifier.height(120.dp))
}
