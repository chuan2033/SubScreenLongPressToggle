package com.Axl.subscreenhook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.Axl.subscreenhook.app.ModuleApp
import com.Axl.subscreenhook.ui.theme.HomeUiTokens
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun StatusCard(serviceAvailable: Boolean, moduleVersion: String) {
    val statusColor = if (serviceAvailable) {
        MiuixTheme.colorScheme.primary
    } else {
        MiuixTheme.colorScheme.onSurfaceVariantSummary
    }

    val frameworkInfo by remember(serviceAvailable) {
        derivedStateOf {
            if (serviceAvailable) {
                val service = ModuleApp.getService()
                if (service != null) {
                    val version = service.frameworkVersion.ifBlank { "未知" }
                    val versionCode = service.frameworkVersionCode
                    if (versionCode > 0) "$version ($versionCode)" else version
                } else {
                    "未知"
                }
            } else {
                "未激活"
            }
        }
    }

    val frameworkName by remember(serviceAvailable) {
        derivedStateOf {
            if (serviceAvailable) {
                ModuleApp.getService()?.frameworkName?.ifBlank { "" } ?: ""
            } else {
                ""
            }
        }
    }

    CardBlock {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .padding(horizontal = HomeUiTokens.ListHorizontalPadding),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(HomeUiTokens.StatusDotSize)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(modifier = Modifier.width(HomeUiTokens.StatusDotSpacing))
                Text(
                    text = if (serviceAvailable) "已激活" else "未激活",
                    color = statusColor,
                    style = MiuixTheme.textStyles.body1
                )
            }
            if (frameworkName.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .padding(horizontal = HomeUiTokens.ListHorizontalPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "框架",
                        color = MiuixTheme.colorScheme.onSurfaceSecondary,
                        style = MiuixTheme.textStyles.body1,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = frameworkName,
                        color = MiuixTheme.colorScheme.onSurface,
                        style = MiuixTheme.textStyles.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            SettingsInfoRow("框架版本", frameworkInfo, height = 32.dp)
        }
    }
}
