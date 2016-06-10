package com.coderstory.miui_toolkit.XposedModule;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
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

        if (lpparam.packageName.equals("miui.drm"))
        {
            XposedHelpers.findAndHookMethod("miui.drm.DrmManager",lpparam.classLoader, "isPermanentRights", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod("miui.content.res.ThemeRuntimeManager.ThemeReceiver",lpparam.classLoader, "validateTheme", XC_MethodReplacement.returnConstant(true));
        }
        if (lpparam.packageName.equals("com.android.thememanager")) {
            XposedHelpers.findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler",lpparam.classLoader, "isTrialable", XC_MethodReplacement.returnConstant(false));
            XposedHelpers.findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler",lpparam.classLoader, "isLegal", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler", lpparam.classLoader,"isAuthorizedResource", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod("com.android.thememanager.util.ThemeOperationHandler",lpparam.classLoader, "isPermanentRights", XC_MethodReplacement.returnConstant(true));

        }
    }
}
