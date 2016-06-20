package com.coderstory.miui_toolkit.XposedModule;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 去你妹的miui8 代码全review了
 * Created by CoderStory on 2016/6/10.
 */
public class ThemePather2 implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("ThemePatcher2", false)) {
            return;
        }
        XposedBridge.log("miui8主题破解");
        if (lpparam.packageName.equals("miui.drm") || lpparam.packageName.equals("com.miui.system")) {
            //判断是有权限使用
            XposedHelpers.findAndHookMethod("miui.drm.DrmManager", lpparam.classLoader, "isPermanentRights", XC_MethodReplacement.returnConstant(true));

            //验证单个主题的方法
            XposedHelpers.findAndHookMethod("miui.drm.ThemeReceiver", lpparam.classLoader, "validateTheme", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod("miui.content.res.ThemeRuntimeManager.ThemeReceiver", lpparam.classLoader, "validateTheme", XC_MethodReplacement.returnConstant(true));

            //替换主题还原的方法
            XposedHelpers.findAndHookMethod("miui.drm.ThemeReceiverr", lpparam.classLoader, "restoreDefault", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });
            XposedHelpers.findAndHookMethod("miui.content.res.ThemeRuntimeManager", lpparam.classLoader, "restoreDefault", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });

            //DRM验证主题的入口点;
            XposedHelpers.findAndHookMethod("miui.drm.ThemeReceiver", lpparam.classLoader, "onReceive", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });

            XposedHelpers.findAndHookMethod("miui.drm.ThemeReceiver", lpparam.classLoader, "onReceive", new XC_MethodReplacement() {
                @Override
                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                    return null;
                }
            });

        }
        if (lpparam.packageName.equals("com.android.thememanager")) {
            //是否试用 可以不改
            XposedHelpers.findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler", lpparam.classLoader, "isTrialable", XC_MethodReplacement.returnConstant(false));
            //是否合法
            XposedHelpers.findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler", lpparam.classLoader, "isLegal", XC_MethodReplacement.returnConstant(true));
            //是否被篡改（本地导入修改的主题）
            XposedHelpers.findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler", lpparam.classLoader, "isAuthorizedResource", XC_MethodReplacement.returnConstant(true));
            //判断是有权限使用
            XposedHelpers.findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler", lpparam.classLoader, "isPermanentRights", XC_MethodReplacement.returnConstant(true));

        }
    }
}