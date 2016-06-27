package com.coderstory.miui_toolkit.XposedModule;

import android.content.Context;
import java.io.File;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by cc on 2016/6/22.
 */
public class ThemePather3 implements IXposedHookZygoteInit, IXposedHookLoadPackage {

    public static Object getDrmResultSUCCESS() {
        try {
            Class<Enum> localString1 = (Class<Enum>) Class.forName("miui.drm.DrmManager$DrmResult");
            if ("miui.drm.DrmManager$DrmResult" != null) {
                return Enum.valueOf(localString1, "DRM_SUCCESS");
            }
        } catch (ClassNotFoundException localString2) {

        } catch (NoSuchMethodError localString3) {

        } catch (Throwable localString4) {

        }
        return null;
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        if (!startupParam.startsSystemServer) {
            return;
        }
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("ThemePatcher3", false)) {
            return;
        }
        MIUI_DRM();
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("ThemePatcher3", false)) {
            return;
        }
        //  XposedBridge.log("Loaded app: " + lpparam.packageName);
        //  XposedBridge.log("miui8主题破解2");
        if (lpparam.packageName.equals("miui.drm") || lpparam.packageName.equals("com.miui.system") || lpparam.packageName.equals("miui.system")) {
            MIUI_DRM();
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

    public void MIUI_DRM() {
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(getDrmResultSUCCESS())});
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(getDrmResultSUCCESS())});
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, String.class, "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(getDrmResultSUCCESS())});
        findAndHookMethod("miui.drm.DrmManager", "isPermanentRights", new Object[]{File.class, XC_MethodReplacement.returnConstant(true)});
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{"miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true)});
    }

    private static void findAndHookMethod(String p1, String p2, Object[] p3) {
        try {
            XposedHelpers.findAndHookMethod(Class.forName(p1), p2, p3);
            return;
        } catch (ClassNotFoundException localString1) {
            return;
        } catch (NoSuchMethodError localString2) {
            return;
        } catch (Throwable localString3) {

        }
    }
}

