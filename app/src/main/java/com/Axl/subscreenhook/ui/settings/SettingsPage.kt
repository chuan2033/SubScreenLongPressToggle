package com.Axl.subscreenhook.ui.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Axl.subscreenhook.BuildConfig
import com.Axl.subscreenhook.ui.components.CardBlock
import com.Axl.subscreenhook.ui.components.SettingsInfoRow
import com.Axl.subscreenhook.ui.theme.HomeUiTokens
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.utils.PressFeedbackType

@Composable
internal fun SettingsPage(
    disableLongPress: Boolean,
    floatingNavBar: Boolean,
    onFloatingNavBarChange: (Boolean) -> Unit
) {
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
        SettingsInfoRow("界面框架", "Miuix ${BuildConfig.MIUIX_VERSION}")
        SettingsInfoRow("长按禁用", if (disableLongPress) "开启" else "关闭")
        SettingsInfoRow("底栏样式", if (floatingNavBar) "悬浮底栏" else "普通底栏")
    }

    SmallTitle(text = "行为说明", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock {
        SettingsInfoRow("开启语义", "拦截长按进入编辑态")
        SettingsInfoRow("关闭语义", "恢复原厂长按行为")
    }

    Spacer(modifier = Modifier.height(120.dp))
}
