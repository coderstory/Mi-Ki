package com.coderstory.miui_toolkit.XposedModule;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 检测插件是否被启用
 * Created by cc on 2016/6/20.
 */
public class enableME  implements  IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (lpparam.packageName.equals("com.coderstory.miui_toolkit")) {
            XposedHelpers.findAndHookMethod("com.coderstory.miui_toolkit.MainActivity", lpparam.classLoader,  "getActivatedModuleVersion", new Object[] { XC_MethodReplacement.returnConstant(Integer.valueOf(9)) });
        }
    }

}
