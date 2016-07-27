package com.coderstory.miui_toolkit.XposedModule;

import android.content.Context;

import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class RemoveAds implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    private static XC_LoadPackage.LoadPackageParam loadPackageParam;

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
            throws Throwable {
        loadPackageParam = paramLoadPackageParam;
        patchcode();
    }

    public void patchcode() {

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("RemoveAds", false)) {
            return;
        }
        if (loadPackageParam.packageName.equals("com.miui.cleanmaster")) {

            findAndHookMethod("com.miui.optimizecenter.result.DataModel", loadPackageParam.classLoader, "post", Map.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.setResult("");
                }
            });
            findAndHookMethod("com.miui.optimizecenter.config.MiStat", loadPackageParam.classLoader, "getChannel", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.setResult("international");
                }
            });
        }
        if (loadPackageParam.packageName.equals("com.miui.video")) {
            findAndHookMethod("com.miui.videoplayer.ads.DynamicAd", loadPackageParam.classLoader, "replaceList", List.class, String.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.args[0] = null;
                    paramAnonymousMethodHookParam.args[1] = null;
                }
            });
        }

        if (loadPackageParam.packageName.equals("com.android.fileexplorer")) {
            findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "isAdEnable", Context.class, String.class, XC_MethodReplacement.returnConstant(false));
            findAndHookMethod("com.android.fileexplorer.model.ConfigHelper", loadPackageParam.classLoader, "supportAd", XC_MethodReplacement.returnConstant(false));
        }


        if (loadPackageParam.packageName.equals("com.miui.player")) {
            findAndHookMethod("com.miui.player.util.AdUtils", loadPackageParam.classLoader, "isAdEnable", XC_MethodReplacement.returnConstant(false));
        }

        if (loadPackageParam.packageName.equals("com.miui.core")) {


            findAndHookMethod("miui.os.SystemProperties", loadPackageParam.classLoader, "get", String.class, String.class, new XC_MethodHook() {

                protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("gemini_global");
                    }
                }

                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    if (paramAnonymousMethodHookParam.args[0].toString().equals("ro.product.mod_device")) {
                        paramAnonymousMethodHookParam.setResult("gemini_global");
                    }
                }
            });

            try {
                XposedHelpers.setStaticBooleanField(Class.forName("miui.os.SystemProperties.Build"), "IS_CM_CUSTOMIZATION_TEST", true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return;
        }

        if (loadPackageParam.packageName.equals("com.android.providers.downloads.ui")) {


            findAndHookMethod("com.android.providers.downloads.ui.recommend.config.ADConfig", loadPackageParam.classLoader, "OSSupportAD", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.setResult(false);

                }
            });
            XposedBridge.log("开始修改下载管理");
//  if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//isTablet  isInternationalBuilder isCmTestBuilder
            findAndHookMethod("com.android.providers.downloads.ui.utils.BuildUtils", loadPackageParam.classLoader, "isCmTestBuilder", XC_MethodReplacement.returnConstant(true));
//                    XposedHelpers.findAndHookMethod("com.android.providers.downloads.ui.setting.DPSharePreferenceInstance", paramLoadPackageParam.classLoader, "getXunleiUsagePermission", XC_MethodReplacement.returnConstant(true));
//                    XposedHelpers.findAndHookMethod("com.android.providers.downloads.ui.setting.DPSharePreferenceInstance", paramLoadPackageParam.classLoader, "getRealXunleiUsagePermission", XC_MethodReplacement.returnConstant(true));
//
//  }
        }

        if (loadPackageParam.packageName.equals("com.miui.weather2")) {
            findAndHookMethod("com.miui.weather2.tools.ToolUtils", loadPackageParam.classLoader, "checkCommericalStatue", Context.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.setResult(false);
                }
            });
//            findAndHookMethod("com.miui.weather2.tools.ToolUtils", loadPackageParam.classLoader, "isCurrentlyUsingSimplifiedChinese", Context.class, new XC_MethodHook() {
//                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
//                        throws Throwable {
//                    paramAnonymousMethodHookParam.setResult(false);
//                }
//            });
            return;
        }
    }

    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam)
            throws Throwable {
    }

    private static void findAndHookMethod(String p1, ClassLoader lpparam, String p2, Object... parameterTypesAndCallback) {
        try {
            XposedHelpers.findAndHookMethod(p1, lpparam, p2, parameterTypesAndCallback);
            return;

        } catch (NoSuchMethodError localString2) {
            XposedBridge.log(localString2.toString());
            return;
        } catch (Throwable localString3) {
            XposedBridge.log(localString3.toString());
        }
    }

}

