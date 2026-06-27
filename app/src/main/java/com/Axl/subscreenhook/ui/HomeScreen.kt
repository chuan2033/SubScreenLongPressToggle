package com.Axl.subscreenhook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.Axl.subscreenhook.ui.about.AboutPage
import com.Axl.subscreenhook.ui.home.HomePage
import com.Axl.subscreenhook.ui.settings.SettingsPage
import com.Axl.subscreenhook.ui.system.ApplyStatusBarAppearance
import com.Axl.subscreenhook.ui.theme.HomeUiTokens
import top.yukonga.miuix.kmp.basic.FloatingNavigationBar
import top.yukonga.miuix.kmp.basic.FloatingNavigationBarItem
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.basic.rememberTopAppBarState
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.GridView
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.theme.MiuixTheme

private val navItems = listOf(
    "主页" to MiuixIcons.GridView,
    "配置" to MiuixIcons.Settings,
    "关于" to MiuixIcons.Info
)

@Composable
internal fun HomeScreen(
    disableLongPress: Boolean,
    removeWallpaperLimit: Boolean,
    serviceAvailable: Boolean,
    floatingNavBar: Boolean,
    onDisableLongPressChange: (Boolean) -> Unit,
    onRemoveWallpaperLimitChange: (Boolean) -> Unit,
    onFloatingNavBarChange: (Boolean) -> Unit
) {
    ApplyStatusBarAppearance()
    var selected by remember { mutableIntStateOf(0) }
    val scrollBehavior = MiuixScrollBehavior(rememberTopAppBarState())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MiuixTheme.colorScheme.background)
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = MiuixTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = navItems[selected].first,
                    largeTitle = navItems[selected].first,
                    color = MiuixTheme.colorScheme.background,
                    titleColor = MiuixTheme.colorScheme.onBackground,
                    largeTitleColor = MiuixTheme.colorScheme.onBackground,
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                if (!floatingNavBar) {
                    NavigationBar {
                        navItems.forEachIndexed { index, (label, icon) ->
                            NavigationBarItem(
                                selected = selected == index,
                                onClick = { selected = index },
                                icon = icon,
                                label = label
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MiuixTheme.colorScheme.background)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = HomeUiTokens.ScreenHorizontalPadding),
                verticalArrangement = Arrangement.spacedBy(HomeUiTokens.SectionSpacing)
            ) {
                when (selected) {
                    0 -> HomePage(
                        disableLongPress = disableLongPress,
                        removeWallpaperLimit = removeWallpaperLimit,
                        floatingNavBar = floatingNavBar,
                        serviceAvailable = serviceAvailable,
                        onDisableLongPressChange = onDisableLongPressChange,
                        onRemoveWallpaperLimitChange = onRemoveWallpaperLimitChange,
                        onFloatingNavBarChange = onFloatingNavBarChange
                    )
                    1 -> SettingsPage()
                    2 -> AboutPage()
                }
            }
        }

        if (floatingNavBar) {
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                FloatingNavigationBar {
                    navItems.forEachIndexed { index, (label, icon) ->
                        FloatingNavigationBarItem(
                            selected = selected == index,
                            onClick = { selected = index },
                            icon = icon,
                            label = label
                        )
                    }
                }
            }
        }
    }
}
