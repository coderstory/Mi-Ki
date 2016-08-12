package com.coderstory.miui_toolkit.XposedModule;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;


public class RemoveSearchBar implements IXposedHookInitPackageResources {

    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam paramInitPackageResourcesParam)
            throws Throwable {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (prefs.getBoolean("RemoveSearchBar", false)) {
            if (paramInitPackageResourcesParam.packageName.equals("com.android.systemui")) {
                paramInitPackageResourcesParam.res.setReplacement(paramInitPackageResourcesParam.packageName, "bool", "config_show_statusbar_search", false);
            }
        }
    }
}
