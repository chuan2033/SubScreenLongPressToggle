# AGENTS.md — AI 交接指南

## 项目概述

`SubScreenLongPressToggle` 是一个 LSPosed Xposed 模块，拦截小米背屏（`com.xiaomi.subscreencenter`）的长按编辑入口。

## 技术栈

- 语言：Java（Hook/Bridge）+ Kotlin（UI）
- 构建：Gradle 9.5.1 + AGP 9.2.1
- UI：Jetpack Compose + Miuix 0.9.2（含 blur）
- Hook：Modern Xposed API 102

## 构建命令

```powershell
$env:JAVA_HOME='D:\RuanJian\Android\Android Studio\jbr'
$env:ANDROID_SDK_ROOT='D:\RuanJian\Android\Sdk'
$env:ANDROID_HOME='D:\RuanJian\Android\Sdk'
& 'D:\codex\背屏模块\SubScreenLongPressToggle\gradlew.bat' --no-daemon --console=plain :app:assembleDebug
```

## 安装到手机

```powershell
& 'D:\RuanJian\Android\Sdk\platform-tools\adb.exe' install -r 'D:\codex\背屏模块\SubScreenLongPressToggle\app\build\outputs\apk\debug\app-debug.apk'
```

## 关键文件

| 文件 | 职责 |
|------|------|
| `hook/ModuleMain.java` | Hook 主入口，防重入保护 |
| `bridge/PrefsBridge.java` | SharedPreferences 桥接（boolean + int） |
| `bridge/SettingsSyncBridge.java` | 广播同步桥 |
| `bridge/SettingChangedReceiver.java` | 静态广播接收器 |
| `common/Constants.java` | 混淆类名/方法名 + 常量 |
| `app/ModuleApp.java` | Application + XposedService 生命周期 |
| `ui/HomeScreen.kt` | 页面容器 + 底栏（悬浮底栏通过 Box 叠加） |
| `ui/system/StatusBarAppearance.kt` | 状态栏适配 |

## 已修复的审计项

- Hook 防重入（双检查锁）
- 广播类型安全（boolean/int 统一协议）
- 状态栏 API 30+ 适配
- 深色模式适配
- 文本溢出保护
- 关于页 URL 可点击
- CardBlock 按压反馈：Tilt（纯展示）/ None（交互控件）
- 悬浮底栏 Box 叠加模式（不在 Scaffold bottomBar）

## 注意事项

- Hook 目标使用 R8 混淆名（`Z1.t`/`Z1.v`），目标应用更新需重新反混淆
- Release 构建启用 ProGuard，关键类已 keep
- miuix-blur 用于 iOS-like 底栏的毛玻璃效果（已移除，当前使用 miuix FloatingNavigationBar）
