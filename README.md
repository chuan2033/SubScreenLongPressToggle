# 背屏助手 LSPosed 模块

这是一个针对小米背屏的 **Modern Xposed API 102** 模块工程，提供以下功能：

- 拦截背屏长按进入编辑态
- 去除主题商店背屏壁纸数量限制（15 张上限）

## 模块行为

- 模块 UI 使用 Miuix Compose 实现，包含 `主页`、`关于`
- 功能设置：`禁用背屏长按切换壁纸`（默认开启）
- 壁纸限制：`去除背屏壁纸数量限制`（默认开启）
- 导航栏切换：`悬浮底栏` / `普通底栏`
- 双主题支持：自动跟随系统深色/浅色模式

## Hook 结论

### 背屏长按禁用

目标包：`com.xiaomi.subscreencenter`

- `Z1.t#e(MotionEvent)` / `Z1.v#g(MotionEvent)`：长按入口判断
- `Z1.t#run()` / `Z1.v#run()`：长按动作执行
- `Z1.v#f(MotionEvent)`：新版手势触摸处理链

### 壁纸数量限制

目标包：`com.android.thememanager`

- `RearScreenDetailViewModel.o5(List)`：壁纸数量检查（原限制 15 张）

**注意**：Hook 目标使用 R8 混淆后的类名/方法名，目标应用更新时需重新反混淆确认。

## 项目布局

```text
app/src/main/java/com/Axl/subscreenhook/
├── app/       ModuleApp.java - Application 与 XposedService 生命周期
├── bridge/    PrefsBridge / SettingsSyncBridge / SettingChangedReceiver
├── common/    Constants.java - 目标包、混淆类名、prefs key
├── hook/      ModuleMain.java - Hook 主入口（双进程）
└── ui/        Compose 页面、组件、导航和系统 UI 辅助
```

## 构建环境

- AGP `9.2.1`
- Kotlin Compose Plugin `2.3.21`
- JDK `21`
- compileSdk `37` / targetSdk `35` / minSdk `26`

## 依赖版本

- Miuix UI `0.9.2`
- Miuix Preference `0.9.2`
- Miuix Icons `0.9.2`
- Modern Xposed API `102.0.0`

## 本地构建

```powershell
$env:JAVA_HOME='D:\RuanJian\Android\Android Studio\jbr'
$env:ANDROID_SDK_ROOT='D:\RuanJian\Android\Sdk'
$env:ANDROID_HOME='D:\RuanJian\Android\Sdk'

& '.\gradlew.bat' :app:assembleDebug --no-daemon --console=plain
```

## 已知限制

- Hook 点位于长按进入编辑态的入口，会屏蔽整条长按编辑链路
- 壁纸数量限制解除需要在 LSPosed 中将主题商店加入作用域

## Hook 目标说明

Hook 目标使用混淆后的类名和方法名，来自目标 APK 的 R8 混淆结果。

- 背屏 APK 版本：`RELEASE-1.0.2605272226`
- 混淆注释在 `common/Constants.java` 中
- 目标应用更新时需重新反混淆确认

## 混淆配置

- Release：`minifyEnabled = false`，与 LSPosed/KSU 等主流 Xposed 模块一致
- Debug：`minifyEnabled = false`

## API 方案

使用 [libxposed/api](https://github.com/libxposed/api) `102.0.0`，静态作用域包含 `com.xiaomi.subscreencenter` 和 `com.android.thememanager`。
