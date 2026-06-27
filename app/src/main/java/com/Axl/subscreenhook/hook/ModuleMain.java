package com.Axl.subscreenhook.hook;

import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Method;

import com.Axl.subscreenhook.bridge.PrefsBridge;
import com.Axl.subscreenhook.bridge.SettingsSyncBridge;
import com.Axl.subscreenhook.common.Constants;
import io.github.libxposed.api.XposedModule;

public class ModuleMain extends XposedModule {
    private static volatile ModuleMain runningInstance;
    private volatile boolean hooksInstalled = false;
    private volatile boolean hookContextReady = false;

    @Nullable
    public static ModuleMain getRunningInstance() {
        return runningInstance;
    }

    @Override
    public void onModuleLoaded(@NonNull ModuleLoadedParam param) {
        runningInstance = this;
        log(Log.INFO, Constants.LOG_TAG, "onModuleLoaded: " + param.getProcessName());
    }

    private volatile boolean themeStoreHooksInstalled = false;

    @Override
    public void onPackageReady(@NonNull PackageReadyParam param) {
        String packageName = param.getPackageName();

        if (Constants.TARGET_PACKAGE.equals(packageName)) {
            if (hooksInstalled) return;
            synchronized (this) {
                if (hooksInstalled) return;
                try {
                    installHookContextBootstrap(param.getClassLoader());
                    installLongPressHooks(param.getClassLoader());
                    hooksInstalled = true;
                    log(Log.INFO, Constants.LOG_TAG, "Hooks installed for " + Constants.TARGET_PACKAGE);
                } catch (Throwable throwable) {
                    log(Log.ERROR, Constants.LOG_TAG, "Failed to install hooks", throwable);
                }
            }
            return;
        }

        if (Constants.THEME_STORE_PACKAGE.equals(packageName)) {
            if (themeStoreHooksInstalled) return;
            synchronized (this) {
                if (themeStoreHooksInstalled) return;
                try {
                    installWallpaperLimitHook(param.getClassLoader());
                    themeStoreHooksInstalled = true;
                    log(Log.INFO, Constants.LOG_TAG, "Theme store hooks installed");
                } catch (Throwable throwable) {
                    log(Log.ERROR, Constants.LOG_TAG, "Failed to install theme store hooks", throwable);
                }
            }
        }
    }

    private void installHookContextBootstrap(@NonNull ClassLoader classLoader) {
        Class<?> mainPanelClass = findClass(Constants.MAIN_PANEL_CLASS, classLoader);
        if (mainPanelClass == null) {
            log(Log.WARN, Constants.LOG_TAG, "Hook target missing: " + Constants.MAIN_PANEL_CLASS);
            return;
        }
        hookMethodIfPresent(
                mainPanelClass,
                "dispatchTouchEvent",
                new Class[]{MotionEvent.class},
                "MainPanel#dispatchTouchEvent",
                chain -> {
                    if (!hookContextReady) {
                        Object thisObject = chain.getThisObject();
                        if (thisObject instanceof android.view.View view) {
                            PrefsBridge.setHookContext(view.getContext());
                            SettingsSyncBridge.ensureHookReceiverInstalled(view.getContext());
                            hookContextReady = true;
                        }
                    }
                    return chain.proceed();
                }
        );
    }

    private void installLongPressHooks(@NonNull ClassLoader classLoader) {
        Class<?> newGestureClass = findClass(Constants.HOOK_CLASS_LONG_PRESS_NEW, classLoader);
        if (newGestureClass != null) {
            hookLongPressMethod(newGestureClass, Constants.HOOK_METHOD_GATE_NEW, new Class[]{MotionEvent.class}, false);
            hookLongPressMethod(newGestureClass, Constants.HOOK_METHOD_LONG_PRESS_TOUCH_NEW, new Class[]{MotionEvent.class}, null);
            hookLongPressMethod(newGestureClass, Constants.HOOK_METHOD_RUN, new Class[]{}, null);
        }

        Class<?> legacyGestureClass = findClass(Constants.HOOK_CLASS, classLoader);
        if (legacyGestureClass != null && legacyGestureClass != newGestureClass) {
            hookLongPressMethod(legacyGestureClass, Constants.HOOK_METHOD_GATE, new Class[]{MotionEvent.class}, false);
            hookLongPressMethod(legacyGestureClass, Constants.HOOK_METHOD_RUN, new Class[]{}, null);
        }
    }

    private void installWallpaperLimitHook(@NonNull ClassLoader classLoader) {
        Class<?> viewModelClass = findClass(Constants.THEME_REAR_VIEWMODEL_CLASS, classLoader);
        if (viewModelClass == null) {
            log(Log.WARN, Constants.LOG_TAG, "Theme hook target missing: " + Constants.THEME_REAR_VIEWMODEL_CLASS);
            return;
        }
        hookMethodIfPresent(
                viewModelClass,
                Constants.THEME_APPLY_CHECK_METHOD,
                new Class[]{java.util.List.class},
                "RearScreenDetailViewModel#o5",
                chain -> {
                    if (PrefsBridge.shouldRemoveWallpaperLimit()) {
                        return true;
                    }
                    return chain.proceed();
                }
        );
    }

    private void hookLongPressMethod(
            @NonNull Class<?> targetClass,
            @NonNull String methodName,
            @NonNull Class<?>[] parameterTypes,
            @Nullable Object blockedReturn
    ) {
        hookMethodIfPresent(
                targetClass,
                methodName,
                parameterTypes,
                targetClass.getName() + "#" + methodName,
                chain -> {
                    if (PrefsBridge.shouldBlockLongPressEdit(this)) {
                        return blockedReturn;
                    }
                    return chain.proceed();
                }
        );
    }

    private void hookMethodIfPresent(
            @NonNull Class<?> targetClass,
            @NonNull String methodName,
            @NonNull Class<?>[] parameterTypes,
            @NonNull String label,
            @NonNull HookCallback callback
    ) {
        hookMethodIfPresent(findDeclaredMethod(targetClass, methodName, parameterTypes), label, callback);
    }

    private void hookMethodIfPresent(
            @Nullable Method method,
            @NonNull String label,
            @NonNull HookCallback callback
    ) {
        if (method == null) {
            log(Log.WARN, Constants.LOG_TAG, "Hook target missing: " + label);
            return;
        }
        try {
            hook(method)
                    .setExceptionMode(ExceptionMode.PROTECTIVE)
                    .intercept(callback::onHook);
            log(Log.DEBUG, Constants.LOG_TAG, "Hook installed: " + label);
        } catch (Throwable throwable) {
            log(Log.ERROR, Constants.LOG_TAG, "Hook install failed: " + label, throwable);
        }
    }

    @Nullable
    private static Class<?> findClass(@NonNull String className, @NonNull ClassLoader classLoader) {
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Nullable
    private static Method findDeclaredMethod(
            @NonNull Class<?> targetClass,
            @NonNull String methodName,
            @NonNull Class<?>... parameterTypes
    ) {
        try {
            Method method = targetClass.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private interface HookCallback {
        Object onHook(Chain chain) throws Throwable;
    }
}
