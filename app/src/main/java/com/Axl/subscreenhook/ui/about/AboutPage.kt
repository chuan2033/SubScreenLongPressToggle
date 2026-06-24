package com.Axl.subscreenhook.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.Axl.subscreenhook.BuildConfig
import com.Axl.subscreenhook.R
import com.Axl.subscreenhook.ui.components.AboutArrowPreference
import com.Axl.subscreenhook.ui.components.CardBlock
import com.Axl.subscreenhook.ui.components.SettingsInfoRow
import com.Axl.subscreenhook.ui.theme.HomeUiTokens
import com.Axl.subscreenhook.ui.util.currentDeviceName
import com.Axl.subscreenhook.ui.util.currentSystemVersion
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.PressFeedbackType

@Composable
internal fun AboutPage() {
    AboutHeader()

    SmallTitle(text = "开发者", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock(pressFeedbackType = PressFeedbackType.None) {
        AboutArrowPreference("AxlQ", "GitHub: chuan2033", "https://github.com/chuan2033")
    }

    SmallTitle(text = "当前系统信息", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock {
        SettingsInfoRow("机型", currentDeviceName())
        SettingsInfoRow("系统版本", currentSystemVersion())
    }

    SmallTitle(text = "关于模块", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock {
        SettingsInfoRow("模块版本", BuildConfig.VERSION_NAME)
        SettingsInfoRow("API 版本", "Modern Xposed API ${io.github.libxposed.api.XposedInterface.LIB_API}")
    }

    SmallTitle(text = "引用", insideMargin = PaddingValues(horizontal = HomeUiTokens.ListHorizontalPadding))
    CardBlock(pressFeedbackType = PressFeedbackType.None) {
        AboutArrowPreference(
            "本项目使用 Miuix UI 组件构建",
            "miuix-kotlin-multiplatform (Apache-2.0)",
            "https://github.com/chuan2033/miuix"
        )
        AboutArrowPreference(
            "开源许可",
            "查看依赖与许可证信息",
            "https://github.com/chuan2033/SubScreenLongPressToggle"
        )
        AboutArrowPreference(
            "参考项目",
            "InstallerX Revived",
            "https://github.com/wxxsfxyzm/InstallerX-Revived"
        )
    }
}

@Composable
private fun AboutHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = HomeUiTokens.AboutHeaderTopPadding,
                bottom = HomeUiTokens.AboutHeaderBottomPadding
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(HomeUiTokens.AboutLogoSize)
                .clip(RoundedCornerShape(HomeUiTokens.AboutLogoCornerRadius)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Icon",
                modifier = Modifier.size(HomeUiTokens.AboutLogoSize),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(HomeUiTokens.AboutHeaderSpacing))
        Text(
            text = "背屏助手",
            color = MiuixTheme.colorScheme.onBackground,
            style = MiuixTheme.textStyles.title1
        )
        Spacer(modifier = Modifier.height(HomeUiTokens.AboutVersionSpacing))
        Text(
            text = "版本 ${BuildConfig.VERSION_NAME}",
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
            style = MiuixTheme.textStyles.body2
        )
    }
}
