package com.coderstory.miui_toolkit.XposedModule;

import android.os.Bundle;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by cc on 2016/6/17.
 */
public class miuiRoot implements IXposedHookInitPackageResources, IXposedHookLoadPackage, IXposedHookZygoteInit{
    //public static TextView WarningText;
   // public static Button accept;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.miui.securitycenter")) {
            return;
        }
        //创建弹出ROOT警告activity的方法
        XposedHelpers.findAndHookMethod("com.miui.permcenter.root.RootApplyActivity", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook()
        {
            protected void afterHookedMethod(MethodHookParam paramAnonymousMethodHookParam)
                    throws Throwable
            {
                XposedHelpers.setIntField(paramAnonymousMethodHookParam.thisObject, "TK", 5);
               // if (miuiRoot.accept == null) {
               //     return;
               // }
                //模拟用户点击确定 5次
               // int i = 0;
                //while (i < 5)
                //{
                //    miuiRoot.accept.performClick();
                //    i += 1;
                //}
            }
        });
        //这个方法是修改每次点击确定时显示不同的警告文字的
//        XposedHelpers.findAndHookMethod("com.miui.permcenter.root.c", loadPackageParam.classLoader, "handleMessage", new Object[] { Message.class, new XC_MethodReplacement()
//       {
//            protected Object replaceHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
//                    throws Throwable
//            {
//                return null;
//            }
//        } });
   }


    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam initPackageResourcesParam) throws Throwable {
//        if (!initPackageResourcesParam.packageName.equals("com.miui.securitycenter")) {
//            return;
//        }
//        initPackageResourcesParam.res.hookLayout("com.miui.securitycenter", "layout", "pm_activity_root_apply", new XC_LayoutInflated()
//        {
//            public void handleLayoutInflated(XC_LayoutInflated.LayoutInflatedParam paramAnonymousLayoutInflatedParam)
//                    throws Throwable
//            {
//                miuiRoot.accept = (Button)paramAnonymousLayoutInflatedParam.view.findViewById(paramAnonymousLayoutInflatedParam.res.getIdentifier("accept", "id", "com.miui.securitycenter"));
//                miuiRoot.WarningText = (TextView)paramAnonymousLayoutInflatedParam.view.findViewById(paramAnonymousLayoutInflatedParam.res.getIdentifier("warning_info", "id", "com.miui.securitycenter"));
//                if (miuiRoot.WarningText != null) {
//                    miuiRoot.WarningText.setLines(6);
//                }
//            }
//        });
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

    }
}
