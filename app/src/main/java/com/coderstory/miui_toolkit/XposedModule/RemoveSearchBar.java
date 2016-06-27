package com.coderstory.miui_toolkit.XposedModule;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;

/**
 * 移除miui的下拉菜单搜索框
 * Created by CoderStory on 2016/6/4.
 */
public class RemoveSearchBar implements IXposedHookInitPackageResources {

    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam paramInitPackageResourcesParam)
            throws Throwable {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("RemoveSearchBar", false)) {
            return;
        }

        if (paramInitPackageResourcesParam.packageName.equals("com.android.systemui")) {
            //XposedBridge.log("去除搜索栏");
            paramInitPackageResourcesParam.res.setReplacement(paramInitPackageResourcesParam.packageName, "bool", "config_show_statusbar_search", false);
        }
    }
}
