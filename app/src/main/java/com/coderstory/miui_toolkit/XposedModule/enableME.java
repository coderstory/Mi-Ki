package com.coderstory.miui_toolkit.XposedModule;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 检测插件是否被启用
 * Created by cc on 2016/6/20.
 */
public class enableME  implements IXposedHookZygoteInit, IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //patchCodeTwo("com.coderstory.miui_toolkit.MainActivity", "checkXp", new Object[]{String.class, File.class, XC_MethodReplacement.returnConstant(Boolean.valueOf(true))});

//        if (lpparam.packageName.equals("com.coderstory.miui_toolkit")) {
//            XposedHelpers.findAndHookMethod(MainActivity.class, "checkXp", new XC_MethodReplacement() {
//                @Override
//                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
//                    return true;
//                }
//            });
        if (!lpparam.packageName.equals("com.coderstory.miui_toolkit")) {
            return;
        }
            XposedHelpers.findAndHookMethod(XposedHelpers.findClass("com.coderstory.miui_toolkit.MainActivity", lpparam.classLoader), "b",
                    new XC_MethodHook()
                    {
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable
                        {
                            XposedBridge.log("HOOK自生");
                            Object object=(Object)true;
                            param.setResult(object);
                        }

                    });
      //  }
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
    }
}
