package com.coderstory.toolkit.XposedModule;

import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

/**
 * 禁止miui检测系统更新
 * Created by CoderStory on 2016/6/4.
 */
public class NoUpdate implements IXposedHookZygoteInit {


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        XSharedPreferences prefs = new XSharedPreferences("com.coderstory.miui_toolkit", "UserSettings");
        prefs.makeWorldReadable();
        prefs.reload();
        if (!prefs.getBoolean("NoUpdate", false)) {
            return;
        }
        XposedBridge.log("屏蔽系统更新");
    }
}
