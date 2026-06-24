# ProGuard rules for SubScreenLongPressToggle

# Xposed 模块入口 - 不混淆
-keep class cn.axlq.subscreenhook.hook.ModuleMain { *; }

# Bridge 层 - 不混淆（UI 进程与 Hook 进程通信）
-keep class cn.axlq.subscreenhook.bridge.** { *; }

# Application - 不混淆
-keep class cn.axlq.subscreenhook.app.ModuleApp { *; }

# Constants - 不混淆（Hook 点配置）
-keep class cn.axlq.subscreenhook.common.Constants { *; }

# Xposed API (recommended for API 102)
-dontwarn io.github.libxposed.annotation.**
-adaptresourcefilecontents META-INF/xposed/java_init.list
-keep,allowoptimization,allowobfuscation public class * extends io.github.libxposed.api.XposedModule {
    public <init>();
}

# Miuix Compose UI
-keep class top.yukonga.miuix.** { *; }
-dontwarn top.yukonga.miuix.**

