package com.coderstory.toolkit.XposedModule;

import android.content.Context;

import java.io.File;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * miui主题破解
 * Created by CoderStory on 2016/6/4.
 */
public class ThemePather implements IXposedHookLoadPackage {
    private static ClassLoader mClassLoader;


    private static void drmManager() throws NoSuchFieldException {
        XposedBridge.log("DRM破解");
        patchCodeTwo("miui.drm.DrmManager", "isLegal", Context.class, String.class, "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal", Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isLegal", Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isPermanentRights", "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.drm.DrmManager", "isPermanentRights", File.class, XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.resourcebrowser.view.ResourceOperationHandler", "isPermanentRights", XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.resourcebrowser.view.ResourceOperationHandler", "isAuthorizedResource", XC_MethodReplacement.returnConstant(true));
        patchCodeTwo("miui.resourcebrowser.model.ResourceOnlineProperties", "setProductBought", Boolean.TYPE, new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0]=true;
               // arrayOfObject[0] = Boolean.valueOf(true);
                paramAnonymousMethodHookParam.args = arrayOfObject;
                return XposedBridge.invokeOriginalMethod(paramAnonymousMethodHookParam.method, paramAnonymousMethodHookParam.thisObject, arrayOfObject);
            }
        });
        patchCodeTwo("miui.resourcebrowser.view.ResourceOperationHandler", "isLegal", new XC_MethodReplacement() {
            protected Object replaceHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                return true;
            }
        });
        patchCodeTwo("miui.resourcebrowser.view.ResourceOperationHandler", "onCheckResourceRightEventBeforeRealApply", new XC_MethodHook() {
            protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable {
                XposedHelpers.setBooleanField(paramAnonymousMethodHookParam.thisObject, "mIsLegal", true);
            }
        });
    }

    private static void patchCodeTwo(String paramString1, String paramString2, Object... paramVarArgs) {
        try {
            Class.forName(paramString1, false, mClassLoader);
            XposedHelpers.findAndHookMethod(paramString1, mClassLoader, paramString2, paramVarArgs);
        } catch (Exception aa) {
            XposedBridge.log(aa.getMessage());
        }
    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
            throws Throwable {
        // XposedBridge.log("进入主题破解");

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();

        if (!prefs.getBoolean("ThemePatcher", false)) {
            return;
        }
        //  XposedBridge.log("开启主题破解");
        mClassLoader = paramLoadPackageParam.classLoader;
        if (paramLoadPackageParam.packageName.equals("com.android.thememanager")) {
            //  XposedBridge.log("开始");
            drmManager();
        }
    }


}
