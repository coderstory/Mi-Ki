package com.coderstory.miui_toolkit;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.coderstory.miui_toolkit.tools.HostsHelper;
import com.coderstory.miui_toolkit.tools.SuHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
        }
        prefs = getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE);
        editor = prefs.edit();
        loadSettings(this);
    }

    /*初始化每一个布局上的按钮的状态并绑定事件
     */
    private void loadSettings(MainActivity mainActivity) {
        Boolean SetValue;
        Switch SwitchBtn;

        //移除搜索框
        SetValue = prefs.getBoolean("RemoveSearchBar", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.RemoveSearchBar);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "RemoveSearchBar");

        //miui去广告
        SetValue = prefs.getBoolean("RemoveAds", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.RemoveAds);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "RemoveAds");

        //主题破解 miui7
        SetValue = prefs.getBoolean("ThemePatcher", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.ThemePatcher);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "ThemePatcher");

        //主题破解 miui8
        SetValue = prefs.getBoolean("ThemePatcher3", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.ThemePatcher3);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "ThemePatcher3");

        //隐藏图标
        SetValue = prefs.getBoolean("switchIcon", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.switchIcon);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        //hosts去广告
        initControl(SwitchBtn, "switchIcon");
        SetValue = prefs.getBoolean("RemoveAdshosts", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.RemoveAdshosts);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "RemoveAdshosts");


        //屏蔽商店 音乐 视频
        SetValue = prefs.getBoolean("NoStore", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.NoStore);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "NoStore");


    }
    //初始化每个按钮的事件
    private void initControl(Switch SwitchBtn, final String key) {
        SwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(key, isChecked);
                editor.apply();
                switch (key) {
                    case "switchIcon":
                        if (isChecked) {
                            HideIcon();
                        } else {
                            showIcon();
                        }
                        break;
                    case "RemoveAdshosts":
                        // changeHosts();
                        new MyTask().execute();
                        break;
                    case "NoStore":
                        // changeHosts();
                        new MyTask().execute();
                        break;
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int item_id = item.getItemId();
        switch (item_id) {
            case R.id.hotboot:
                SuHelper.showTips("busybox killall system_server", getString(R.string.Tips_HotBoot), this);
                break;
            case R.id.reboot:
                SuHelper. showTips("reboot", getString(R.string.Tips_Reboot), this);
                break;
            case R.id.about:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
            default:
                return false;
        }
        return true;
    }

    public  void  openAbout(View v){
        Intent intent=new Intent(this,AboutActivity.class);
        startActivity(intent);
    }
    //隐藏图标
    private void HideIcon() {
        ComponentName localComponentName = new ComponentName(this,"com.coderstory.miui_toolkit.SplashActivity");
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.getComponentEnabledSetting(localComponentName);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, "com.coderstory.miui_toolkit.SplashActivity");
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    //显示图标
    private void showIcon() {
        ComponentName localComponentName = new ComponentName(this, "com.coderstory.miui_toolkit.SplashActivity");
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.getComponentEnabledSetting(localComponentName);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, "com.coderstory.miui_toolkit.SplashActivity");
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP);
    }
    //修改hosts的方法
    private void changeHosts() {
        boolean RemoveAdshosts = prefs.getBoolean("RemoveAdshosts", false); //4
        boolean NoStore = prefs.getBoolean("NoStore", false); //4
        Map<String, String> setMap = new HashMap<>();
        if (RemoveAdshosts) {
            setMap.put("RemoveAdshosts", "True");
        } else {
            setMap.put("RemoveAdshosts", "False");
        }
        if (NoStore) {
            setMap.put("NoStore", "True");
        } else {
            setMap.put("NoStore", "False");
        }
        HostsHelper h = new HostsHelper(MainActivity.this, setMap);
        if (!h.execute()) {
            Toast.makeText(MainActivity.this, R.string.Tips_No_Root, Toast.LENGTH_SHORT).show();
            Switch SwitchBtn = (Switch) MainActivity.this.findViewById(R.id.RemoveAdshosts);
            if (SwitchBtn != null) {
                SwitchBtn.setChecked(false);
            }
        }
    }
    //因为hosts修改比较慢 所以改成异步的
    class MyTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
           // setProgressBarIndeterminateVisibility(true);
            showProgress();
        }
        @Override
        protected void onPostExecute(String param) {
            //setProgressBarIndeterminateVisibility(false);
            closeProgress();
        }
        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
        @Override
        protected String doInBackground(String... params) {
            Looper.prepare();
            //  initData();
            changeHosts();
            return null;
        }
    }
    private Dialog dialog;
    protected void showProgress() {
        if (dialog == null) {
            dialog = ProgressDialog.show(this, getString(R.string.Tips_Title), getString(R.string.Tips_Processing));
            dialog.show();
        }
    }
    protected void closeProgress() {
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }
}