# ProGuard rules for SubScreenLongPressToggle

# Xposed module entry
-keep class com.Axl.subscreenhook.hook.ModuleMain { *; }

# Bridge layer
-keep class com.Axl.subscreenhook.bridge.** { *; }

# Application
-keep class com.Axl.subscreenhook.app.ModuleApp { *; }

# Constants
-keep class com.Axl.subscreenhook.common.Constants { *; }

# Xposed API
-dontwarn io.github.libxposed.annotation.**
-adaptresourcefilecontents META-INF/xposed/java_init.list
-keep,allowoptimization,allowobfuscation public class * extends io.github.libxposed.api.XposedModule {
    public <init>();
}

# Xposed Service
-keep class io.github.libxposed.service.** { *; }
-keep class io.github.libxposed.service.XposedServiceHelper$OnServiceListener { *; }
-keep class io.github.libxposed.service.XposedServiceHelper { *; }
-keep class io.github.libxposed.service.XposedService { *; }
-keep class io.github.libxposed.service.XposedProvider { *; }
-keep class io.github.libxposed.api.XposedInterface { *; }

# Miuix
-keep class top.yukonga.miuix.** { *; }
-dontwarn top.yukonga.miuix.**

# UI classes
-keep class com.Axl.subscreenhook.ui.** { *; }
