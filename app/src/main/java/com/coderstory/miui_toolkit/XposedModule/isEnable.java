package com.coderstory.miui_toolkit.XposedModule;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by CoderStory on 2016/7/12.
 */

public class isEnable implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.coderstory.miui_toolkit")) {
            try {

                XposedBridge.log("插件已激活");
                XposedHelpers. findAndHookMethod("com.coderstory.miui_toolkit.MainActivity", lpparam.classLoader, "isEnable",   XC_MethodReplacement.returnConstant(true));
            } catch (Throwable p1) {
                XposedBridge.log(p1);
            }
        }
    }
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }
}
