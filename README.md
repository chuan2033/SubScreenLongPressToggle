# 背屏长按禁用 LSPosed 模块

这是一个针对米17pro系列背屏模块，用于拦截背屏长按进入编辑态的链路。

## 项目布局

```text
app/src/main/java/com/Axl/subscreenhook/
├── app/       ModuleApp.java - Application 与 XposedService 生命周期
├── bridge/    PrefsBridge / SettingsSyncBridge / SettingChangedReceiver
├── common/    Constants.java - 目标包、混淆类名、prefs key
├── hook/      ModuleMain.java - Hook 主入口
└── ui/        Compose 页面、组件、导航和系统 UI 辅助
```
