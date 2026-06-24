package com.Axl.subscreenhook.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.Axl.subscreenhook.ui.theme.HomeUiTokens
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
internal fun LeftAlignedTopAppBar(
    title: String,
    scrollBehavior: ScrollBehavior? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(HomeUiTokens.TopAppBarHeight)
            .padding(horizontal = HomeUiTokens.ListHorizontalPadding)
    ) {
        Text(
            text = title,
            color = MiuixTheme.colorScheme.onBackground,
            style = MiuixTheme.textStyles.title1
        )
    }
}
