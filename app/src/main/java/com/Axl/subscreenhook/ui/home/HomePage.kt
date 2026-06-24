package com.Axl.subscreenhook.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Axl.subscreenhook.BuildConfig
import com.Axl.subscreenhook.common.Constants
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
    serviceAvailable: Boolean,
    onDisableLongPressChange: (Boolean) -> Unit
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
    }

    SmallTitle(text = "运行信息", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock {
        SettingsInfoRow("目标包名", Constants.TARGET_PACKAGE)
        SettingsInfoRow("模块包名", BuildConfig.APPLICATION_ID)
        SettingsInfoRow("默认状态", "开启禁用长按")
    }

    Spacer(modifier = Modifier.height(120.dp))
}
