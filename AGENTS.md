# AGENTS.md — AI 交接指南

## 项目概述

`SubScreenLongPressToggle` 是一个 LSPosed Xposed 模块，拦截小米背屏（`com.xiaomi.subscreencenter`）的长按编辑入口，并去除主题商店（`com.android.thememanager`）的背屏壁纸数量限制。

## 技术栈

- 语言：Java（Hook/Bridge）+ Kotlin（UI）
- 构建：Gradle 9.5.1 + AGP 9.2.1
- UI：Jetpack Compose + Miuix 0.9.2
- Hook：Modern Xposed API 102

## 构建命令

```powershell
$env:JAVA_HOME='D:\RuanJian\Android\Android Studio\jbr'
$env:ANDROID_SDK_ROOT='D:\RuanJian\Android\Sdk'
$env:ANDROID_HOME='D:\RuanJian\Android\Sdk'
& 'D:\codex\背屏模块\SubScreenLongPressToggle\gradlew.bat' --no-daemon --console=plain :app:assembleDebug
```

Release 构建（签名使用 debug keystore）：

```powershell
& 'D:\codex\背屏模块\SubScreenLongPressToggle\gradlew.bat' --no-daemon --console=plain :app:assembleRelease
```

## 安装到手机

```powershell
& 'D:\RuanJian\Android\Sdk\platform-tools\adb.exe' install -r 'D:\codex\背屏模块\SubScreenLongPressToggle\app\build\outputs\apk\debug\app-debug.apk'
```

## 关键文件

| 文件 | 职责 |
|------|------|
| `hook/ModuleMain.java` | Hook 主入口，双进程 hook（背屏 + 主题壁纸），防重入保护 |
| `bridge/PrefsBridge.java` | SharedPreferences 桥接（boolean + int） |
| `bridge/SettingsSyncBridge.java` | 广播同步桥（boolean/int 统一协议） |
| `bridge/SettingChangedReceiver.java` | 静态广播接收器 |
| `common/Constants.java` | 目标包名、混淆类名/方法名 + 常量 |
| `app/ModuleApp.java` | Application + XposedService 生命周期 |
| `ui/HomeScreen.kt` | 页面容器 + 底栏（悬浮底栏通过 Box 叠加） |
| `ui/home/HomePage.kt` | 主页：功能设置 + 导航栏切换 + 当前配置 |
| `ui/about/AboutPage.kt` | 关于页：系统信息 + 项目地址 + 开发者 + 引用 |
| `ui/components/StatusCard.kt` | 激活状态卡片（框架、框架版本） |
| `ui/components/InfoRows.kt` | 信息行组件（InfoRow 上下布局 / SettingsInfoRow 左右布局） |
| `ui/components/PreferenceCards.kt` | 卡片容器 + 关于页箭头偏好 |
| `ui/system/StatusBarAppearance.kt` | 状态栏适配（浅色图标） |
| `ui/util/SystemInfo.kt` | 系统信息读取（机型、HyperOS 版本、安卓版本） |

## 功能模块

1. **禁用背屏长按** — hook `com.xiaomi.subscreencenter` 的手势类 `Z1.t`/`Z1.v`
2. **去除壁纸数量限制** — hook `com.android.thememanager` 的 `RearScreenDetailViewModel.o5()` 方法，绕过 15 张上限

## UI 结构

- 两页结构：`主页` / `关于`
- 主页：激活状态卡片 + 功能设置（长按禁用 + 壁纸限制）+ 导航栏切换 + 当前配置
- 关于页：logo + 开发者 + 系统信息（机型/系统版本/安卓版本）+ 项目地址 + 引用

## 已修复的审计项

- Hook 防重入（双检查锁）
- 广播类型安全（boolean/int 统一协议）
- 状态栏 API 30+ 适配
- 深色模式适配
- 文本溢出保护
- 关于页 URL 可点击
- CardBlock 按压反馈：Tilt（纯展示）/ None（交互控件）
- 悬浮底栏 Box 叠加模式（不在 Scaffold bottomBar）
- Scaffold containerColor 匹配背景色（解决导航栏灰色背景）

## 注意事项

- Hook 目标使用 R8 混淆名（`Z1.t`/`Z1.v`），目标应用更新需重新反混淆
- Release 构建不混淆（与主流 Xposed 模块一致）
- scope.list 包含两个目标包：`com.xiaomi.subscreencenter` 和 `com.android.thememanager`
- 壁纸限制解除默认开启，可通过主页开关控制
- HyperOS 版本通过 `ro.mi.os.version.name` 属性读取
