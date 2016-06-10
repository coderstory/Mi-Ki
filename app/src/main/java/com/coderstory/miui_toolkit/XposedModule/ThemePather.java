package com.coderstory.miui_toolkit.XposedModule;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ThemePather implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("ThemePatcher", false)) {
            return;
        }
        if (lpparam.packageName.equals("miui.drm"))
        {
            XposedHelpers.findAndHookMethod("miui.drm.ThemeReceiver", lpparam.classLoader, "onReceive", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    paramAnonymousMethodHookParam.args[1]="fuck theme";
                }
            });
        }
        if (lpparam.packageName.equals("com.android.thememanager")) {
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler",lpparam.classLoader, "isPermanentRights", XC_MethodReplacement.returnConstant(true));
            XposedHelpers.findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", lpparam.classLoader,"isAuthorizedResource", XC_MethodReplacement.returnConstant(true));
        }
    }

}