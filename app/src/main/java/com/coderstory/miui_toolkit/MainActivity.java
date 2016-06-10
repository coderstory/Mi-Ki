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
import android.net.Uri;
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

import com.coderstory.miui_toolkit.tools.hosts;

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
        if (!prefs.getBoolean("getRoot", false)) {
            showTips("echo 1", getString(R.string.Tips_Need_Root), this);

        }
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
        //核心破解
        SetValue = prefs.getBoolean("CorePatcher", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.CorePatcher);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "CorePatcher");
        //禁止miui检测更新
        SetValue = prefs.getBoolean("NoUpdate", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.NoUpdate);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "NoUpdate");
        //hosts去广告
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

        //主题破解 miui8
        SetValue = prefs.getBoolean("ThemePatcher2", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.ThemePatcher2);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }


        initControl(SwitchBtn, "ThemePatcher");
        //隐藏图标
        SetValue = prefs.getBoolean("switchIcon", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.switchIcon);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "switchIcon");
        SetValue = prefs.getBoolean("RemoveAdshosts", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.RemoveAdshosts);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "RemoveAdshosts");
        //谷歌hosts
        SetValue = prefs.getBoolean("GoogleHosts", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.GoogleHosts);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "GoogleHosts");

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
                showTips("killall system_server", getString(R.string.Tips_HotBoot), this);
                break;
            case R.id.reboot:
                showTips("reboot", getString(R.string.Tips_Reboot), this);
                break;
            default:
                return false;
        }
        return true;
    }

    /*实现弹窗确定执行某条命令*/
    public static void showTips(final String commandText, String messageText, final Context mContext) {
        AlertDialog builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.Tips_Title)
                .setMessage(messageText)
                .setPositiveButton(R.string.Btn_Sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if ("echo 1".equals(commandText)) {
                            editor.putBoolean("getRoot", true);
                            editor.apply();
                        }

                        // String cmd = commandText;
                        try {
                            Runtime.getRuntime().exec(new String[]{"su", "-c", commandText});
                        } catch (IOException e) {
                            Log.d("su", e.getMessage());
                            new AlertDialog.Builder(mContext).setTitle(R.string.Tips_Title_Error).setMessage(
                                    e.getMessage()).setPositiveButton(R.string.Btn_Sure, null).show();
                        }
                    }
                })
                .setNegativeButton(R.string.Btn_Cancel, new DialogInterface.OnClickListener() {
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

    //隐藏图标
    private void HideIcon() {
        ComponentName localComponentName = new ComponentName(this, getClass().getName() + "-Alias");
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.getComponentEnabledSetting(localComponentName);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, getClass().getName() + "-Alias");
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    //显示图标
    private void showIcon() {
        ComponentName localComponentName = new ComponentName(this, getClass().getName() + "-Alias");
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.getComponentEnabledSetting(localComponentName);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, getClass().getName() + "-Alias");
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP);
    }

    //打开我的博客
    public void opneUrl(View view) {
        //  Toast.makeText(this, "",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://blog.coderstory.cn"));
        startActivity(intent);
    }

    //修改hosts的方法
    private void changeHosts() {
        boolean NoUpdate = prefs.getBoolean("NoUpdate", false); //1
        // boolean RemoveAds = prefs.getBoolean("RemoveAds", false); //2
        boolean GoogleHosts = prefs.getBoolean("GoogleHosts", false); //4
        boolean RemoveAdshosts = prefs.getBoolean("RemoveAdshosts", false); //4
        Map<String, String> setMap = new HashMap<>();
        if (NoUpdate) {
            setMap.put("NoUpdate", "True");
        } else {
            setMap.put("NoUpdate", "False");
        }
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
