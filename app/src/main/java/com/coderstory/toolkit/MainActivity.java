package com.coderstory.toolkit;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE);
        editor = prefs.edit();
        loadSettings(this);
    }

    /*load user preferences
     */
    private void loadSettings(MainActivity mainActivity) {

        Boolean SetValue;
        Switch SwitchBtn;

        SetValue = prefs.getBoolean("RemoveSearchBar", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.RemoveSearchBar);
        SwitchBtn.setChecked(SetValue);
        initControl(SwitchBtn, "RemoveSearchBar");

        SetValue = prefs.getBoolean("CorePatcher", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.CorePatcher);
        SwitchBtn.setChecked(SetValue);
        initControl(SwitchBtn, "CorePatcher");

        SetValue = prefs.getBoolean("NoUpdate", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.NoUpdate);
        SwitchBtn.setChecked(SetValue);
        initControl(SwitchBtn, "NoUpdate");

        SetValue = prefs.getBoolean("RemoveAds", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.RemoveAds);
        SwitchBtn.setChecked(SetValue);
        initControl(SwitchBtn, "RemoveAds");

        SetValue = prefs.getBoolean("ThemePatcher", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.ThemePatcher);
        SwitchBtn.setChecked(SetValue);
        initControl(SwitchBtn, "ThemePatcher");


        SetValue = prefs.getBoolean("switchIcon", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.switchIcon);
        SwitchBtn.setChecked(SetValue);
        initControl(SwitchBtn, "switchIcon");

    }

    private void initControl(Switch SwitchBtn, final String key) {
        SwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(key, isChecked);
                editor.apply();
                if (key.equals("switchIcon")) {
                    if (isChecked) {
                        HideIcon();
                    } else {
                        showIcon();
                    }
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
                showTips("killall system_server", "你确定要快速重启吗？(本功能需要busybox支持)", this);
                break;
            case R.id.reboot:
                showTips("reboot", "你确定要重启吗？", this);
                break;
            default:
                return false;
        }
        return true;
    }

    /*实现弹窗确定执行某条命令*/
    public static void showTips(final String commandText, String messageText, final Context mContext) {
        AlertDialog builder = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage(messageText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String cmd = commandText;
                        try {
                            Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
                        } catch (IOException e) {
                            Log.d("su", e.getMessage());
                            new AlertDialog.Builder(mContext).setTitle("错误").setMessage(
                                    e.getMessage()).setPositiveButton("确定", null).show();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消当前对话框
                        dialog.cancel();

                    }
                }).create();
        builder.show();
    }

    private void HideIcon() {
        ComponentName localComponentName = new ComponentName(this, getClass().getName() + "-Alias");
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.getComponentEnabledSetting(localComponentName);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, getClass().getName() + "-Alias");
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    private void showIcon() {


        ComponentName localComponentName = new ComponentName(this, getClass().getName() + "-Alias");
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.getComponentEnabledSetting(localComponentName);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, getClass().getName() + "-Alias");
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP);
    }
    public  void opneUrl(View view){
        //  Toast.makeText(this, "",Toast.LENGTH_LONG).show();
        Intent intent=new Intent( Intent.ACTION_VIEW);
        intent.setData(Uri.parse( "http://blog.coderstory.cn"));
        startActivity(intent);
    }
}
