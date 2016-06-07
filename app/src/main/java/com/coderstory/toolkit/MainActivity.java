package com.coderstory.toolkit;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.coderstory.toolkit.tools.hosts;

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
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }

        prefs = getSharedPreferences("UserSettings", Context.MODE_WORLD_READABLE);
        editor = prefs.edit();
        loadSettings(this);
        if (!prefs.getBoolean("getRoot", false)) {
            showTips("echo 1", "软件运行需要ROOT权限，点击确定开始授权.", this);

        }
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

        SetValue = prefs.getBoolean("RemoveAdshosts", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.RemoveAdshosts);
        SwitchBtn.setChecked(SetValue);
        initControl(SwitchBtn, "RemoveAdshosts");


        SetValue = prefs.getBoolean("GoogleHosts", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.GoogleHosts);
        SwitchBtn.setChecked(SetValue);
        initControl(SwitchBtn, "GoogleHosts");

    }

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
                    case "NoUpdate":
                        // changeHosts();
                        new MyTask().execute();
                        break;
                    case "GoogleHosts":
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

                        if ("echo 1".equals(commandText)) {
                            editor.putBoolean("getRoot", true);
                            editor.apply();
                        }

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
                        if ("echo 1".equals(commandText)) {
                            System.exit(0);
                        }
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

    public void opneUrl(View view) {
        //  Toast.makeText(this, "",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://blog.coderstory.cn"));
        startActivity(intent);
    }

    private void changeHosts() {
        boolean NoUpdate = prefs.getBoolean("NoUpdate", false); //1
       // boolean RemoveAds = prefs.getBoolean("RemoveAds", false); //2
        boolean GoogleHosts = prefs.getBoolean("GoogleHosts", false); //4
        boolean RemoveAdshosts = prefs.getBoolean("RemoveAdshosts", false); //4
        Map<String, String> setMap = new HashMap<String, String>();
        if (NoUpdate) {
            setMap.put("NoUpdate", "True");
        } else {
            setMap.put("NoUpdate", "False");
        }
        //if (RemoveAds) {
        //    setMap.put("RemoveAds", "True");
        //} else {
        //    setMap.put("RemoveAds", "False");
       // }
        if (GoogleHosts) {
            setMap.put("GoogleHosts", "True");
        } else {
            setMap.put("GoogleHosts", "False");
        }
        if (RemoveAdshosts) {
            setMap.put("RemoveAdshosts", "True");
        } else {
            setMap.put("RemoveAdshosts", "False");
        }


        hosts h = new hosts(MainActivity.this, setMap);
        if (!h.execute()) {
            Toast.makeText(MainActivity.this, "未获取Root权限", Toast.LENGTH_SHORT).show();
            Switch SwitchBtn = (Switch) MainActivity.this.findViewById(R.id.RemoveAdshosts);
            SwitchBtn.setChecked(false);
        }
    }

    class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
            showProgress();
        }

        @Override
        protected void onPostExecute(String param) {
            //  showData();
            setProgressBarIndeterminateVisibility(false);
            // adapter.notifyDataSetChanged();
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
//		    dialog.setContentView(R.layout.progress_dialog);
            //    dialog.getWindow().setAttributes(params);
            dialog = ProgressDialog.show(this, "温馨提示", "正在处理中...");
            dialog.show();
        }
    }

    //
    protected void closeProgress() {

        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }
}
