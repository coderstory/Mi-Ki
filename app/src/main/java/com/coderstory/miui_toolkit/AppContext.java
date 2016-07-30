package com.coderstory.miui_toolkit;


import android.app.Application;
import android.content.Context;


public class AppContext extends Application {

    private static AppContext appInstance;
    private Context context;

    public static AppContext getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        appInstance = this;
        context = this.getBaseContext();
//      // 获取当前版本号
//      try {
//          PackageInfo packageInfo = getApplicationContext()
//                  .getPackageManager().getPackageInfo(getPackageName(), 0);
//          Config.localVersion = packageInfo.versionCode;
//          Config.serverVersion = 1;// 假定服务器版本为2，本地版本默认是1
//      } catch (NameNotFoundException e) {
//          e.printStackTrace();
//      }
        initGlobal();
    }

    public void initGlobal() {
        try {
            config.localVersion = getPackageManager().getPackageInfo(
                    getPackageName(), 0).versionCode; // 设置本地版本号
            config.serverVersion = 2;// 假定服务器版本为2，本地版本默认是1--实际开发中是从服务器获取最新版本号，android具体与后端的交互见我另///外的博文
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}