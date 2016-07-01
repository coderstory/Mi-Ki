package com.coderstory.miui_toolkit.XposedModule;


import android.content.Context;

import java.io.File;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class ThemePatherV7 implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam paramLoadPackageParam)
            throws Throwable {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("ThemePatcher", false)) {
            return;
        }
        PatchCode();
    }

    public void initZygote(IXposedHookZygoteInit.StartupParam paramStartupParam) throws Throwable {
    }

    public static void PatchCode() {
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, File.class, File.class, XC_MethodReplacement.returnConstant(getDrmResultSUCCESS())});
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, String.class, File.class, XC_MethodReplacement.returnConstant(getDrmResultSUCCESS())});
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{Context.class, String.class, "miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(getDrmResultSUCCESS())});
        findAndHookMethod("miui.drm.DrmManager", "isPermanentRights", new Object[]{File.class, XC_MethodReplacement.returnConstant(true)});
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{"miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true)});
        findAndHookMethod("miui.drm.DrmManager", "isPermanentRights", new Object[]{File.class, XC_MethodReplacement.returnConstant(true)});
        findAndHookMethod("miui.drm.DrmManager", "isLegal", new Object[]{"miui.drm.DrmManager$RightObject", XC_MethodReplacement.returnConstant(true)});
        findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", "isAuthorizedResource", new Object[]{ XC_MethodReplacement.returnConstant(true)});
        findAndHookMethod("miui.resourcebrowser.view.ResourceOperationHandler", "isPermanentRights", new Object[]{ XC_MethodReplacement.returnConstant(true)});
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

}