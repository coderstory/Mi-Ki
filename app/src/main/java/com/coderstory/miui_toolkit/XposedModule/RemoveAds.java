package com.coderstory.miui_toolkit.XposedModule;

import android.content.Context;

import java.util.Map;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 移除miui内置广告
 * Created by CoderStory on 2016/6/4.
 */
public class RemoveAds implements IXposedHookZygoteInit, IXposedHookLoadPackage {


    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
            throws Throwable {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("RemoveAds", false)) {
            return;
        }
       // XposedBridge.log("删系统广告");
        if (paramLoadPackageParam.packageName.equals("com.android.providers.downloads.ui"))
        {
            try {
                XposedHelpers.findAndHookMethod("com.android.providers.downloads.ui.recommend.config.ADConfig", paramLoadPackageParam.classLoader, "OSSupportAD", new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(false);
                    }
                });
                return;
            } catch (Throwable p1) {
                XposedBridge.log(p1);
                return;
            }
        }
        if (paramLoadPackageParam.packageName.equals("com.miui.weather2")) {
            try {
                XposedHelpers.findAndHookMethod("com.miui.weather2.tools.ToolUtils", paramLoadPackageParam.classLoader, "checkCommericalStatue", Context.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(false);
                    }
                });
                return;
            } catch (Throwable p2) {
                XposedBridge.log(p2);
                return;
            }
        }
        if (paramLoadPackageParam.packageName.equals("com.android.quicksearchbox")) {
            try {
                XposedHelpers.findAndHookMethod("com.android.quicksearchbox.util.HotWordsUtil", paramLoadPackageParam.classLoader, "setHotQueryView", "com.android.quicksearchbox.ui.HotQueryView", new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult(null);
                    }
                });
                return;
            } catch (Throwable p3) {
                XposedBridge.log(p3);
                return;
            }
        }
        if (paramLoadPackageParam.packageName.equals("com.miui.cleanmaster")) {
            try {
                XposedHelpers.findAndHookMethod("com.miui.optimizecenter.result.DataModel", paramLoadPackageParam.classLoader, "post", Map.class, new XC_MethodHook() {
                    protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        paramAnonymousMethodHookParam.setResult("");
                    }
                });
            } catch (Throwable p4) {
                XposedBridge.log(p4);
            }
        }
    }

    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam)
            throws Throwable {
    }
}

