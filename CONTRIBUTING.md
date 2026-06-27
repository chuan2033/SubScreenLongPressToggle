# Contributing

## Before editing

1. Use JDK 21 and Android SDK matching the versions in `README.md`.
2. Build once locally before changing hook targets or package names.
3. Keep all target-package constants and hook method names in `app/src/main/java/com/Axl/subscreenhook/common/Constants.java`.

## Project conventions

- Put hook entry logic under `hook/`.
- Put app and hook synchronization code under `bridge/`.
- Keep UI files under `ui/` and split pages/components instead of growing a single screen file.
- Avoid hard-coding package names or setting keys outside `Constants`.
- Theme store hooks go in `installWallpaperLimitHook()` within `ModuleMain.java`.

## Validation

Run:

```powershell
& '.\gradlew.bat' :app:assembleDebug --no-daemon --console=plain
```

If you change hook behavior, also re-verify on device that:

- the LSPosed scope includes both `com.xiaomi.subscreencenter` and `com.android.thememanager`
- the UI toggle state reaches the hook process
- long-press behavior matches the current toggle value
- wallpaper limit removal works when enabled
