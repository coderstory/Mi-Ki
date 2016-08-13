package com.coderstory.miui_toolkit.Activity;

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
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.coderstory.miui_toolkit.R;
import com.coderstory.miui_toolkit.tools.Hosts.HostsHelper;
import com.coderstory.miui_toolkit.tools.RequestPermissions;
import com.coderstory.miui_toolkit.tools.Root.SuHelper;
import com.coderstory.miui_toolkit.tools.Update.CheckUpdate;
import com.umeng.analytics.MobclickAgent;
import java.util.HashMap;
import java.util.Map;
import static com.coderstory.miui_toolkit.tools.Root.SuHelper.canRunRootCommands;

public class MainActivity extends AppCompatActivity {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

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
        // MobclickAgent.setCatchUncaughtExceptions(false);
        MobclickAgent.setScenarioType(MainActivity.this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);


        if (!isEnable()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle(R.string.Label_Waring);
            dialog.setMessage(R.string.Tips_No_XP_Enable);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.Btn_Sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //System.exit(0);
                }
            });
            dialog.show();
        }

        //第一次运行
        if (!prefs.getBoolean("First", false)) {
            AlertDialog builder = new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.Tips_Title)
                    .setMessage(R.string.Tips_FirstOpen)
                    .setPositiveButton(R.string.Btn_Sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean("First", true);
                            editor.apply();
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, HelperActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.Btn_Cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean("First", true);
                            editor.apply();
                            dialog.cancel();
                        }
                    }).create();
            builder.show();

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle(R.string.Tips_Title);
            dialog.setMessage(R.string.Tips_Start_Get_Root);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.Btn_Sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!canRunRootCommands()) {
                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity.this);
                        dialog2.setTitle(R.string.Label_Waring);
                        dialog2.setMessage(R.string.Tips_No_Root);
                        dialog2.setCancelable(false);
                        dialog2.setPositiveButton(R.string.Btn_Sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.putBoolean("getRoot", true);
                                editor.apply();
                            }
                        });
                        dialog2.show();
                    } else {
                        processRoot();
                        editor.putBoolean("getRoot", true);
                        editor.apply();
                    }
                }
            });
            dialog.show();
            editor.putBoolean("First", true);
            editor.apply();

        }else{
            processRoot();
        }

        // 申请权限并开始检测更新
        if (RequestPermissions.isGrantExternalRW(MainActivity.this)) {
            CheckUpdate CU = new CheckUpdate(MainActivity.this);
            new Thread(CU.networkTask).start();
        }
    }

    /**
     * 根据root授权情况 启用或者禁用某些功能
     */
    private void processRoot() {
        if (canRunRootCommands()) {
            //绑定打开冻结APP页面事件
            TextView tv = (TextView) findViewById(R.id.DisableApp);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DisableAppActivity.class);
                    startActivity(intent);
                }
            });
            tv = (TextView) findViewById(R.id.textView8);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DisableAppActivity.class);
                    startActivity(intent);
                }
            });
        } else {

            // System.exit(0);
            //去除广告
            findViewById(R.id.RemoveAdshosts).setEnabled(false);
            //谷歌hosts  NoStore
            findViewById(R.id.GoogleHosts).setEnabled(false);
            //屏蔽商店 音乐 视频
            findViewById(R.id.NoStore).setEnabled(false);
            //禁止miui检测更新
            findViewById(R.id.NoUpdate).setEnabled(false);

            TextView tv = (TextView) findViewById(R.id.DisableApp);
           // tv.setTextColor(getResources().getColor(R.color.disablefunction));
            tv.setTextColor( ContextCompat.getColor(MainActivity.this, R.color.disablefunction));
            // tv = (TextView) findViewById(R.id.textView8);
            // tv.setTextColor(getResources().getColor(R.color.disablefunction));
        }
    }

    //处理权限申请结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            CheckUpdate CU = new CheckUpdate(MainActivity.this);
            new Thread(CU.networkTask).start();
        } else {
            Toast.makeText(MainActivity.this, R.string.Tips_NO_SDCARD_Permission, Toast.LENGTH_LONG).show();
        }
    }


    private static boolean isEnable() {
        return false;
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
        initControl(SwitchBtn, "ThemePatcher");
        //主题破解 miui8 1
        SetValue = prefs.getBoolean("ThemePatcher2", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.ThemePatcher2);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "ThemePatcher2");
        //主题破解 miui8 1
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
        //谷歌hosts  NoStore
        SetValue = prefs.getBoolean("GoogleHosts", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.GoogleHosts);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "GoogleHosts");

        //屏蔽商店 音乐 视频
        SetValue = prefs.getBoolean("NoStore", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.NoStore);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "NoStore");

        //root 25秒等待
        SetValue = prefs.getBoolean("root", false);
        SwitchBtn = (Switch) mainActivity.findViewById(R.id.root);
        if (SwitchBtn != null) {
            SwitchBtn.setChecked(SetValue);
        }
        initControl(SwitchBtn, "root");


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

                        new MyTask().execute();
                        break;
                    case "NoUpdate":

                        new MyTask().execute();
                        break;
                    case "GoogleHosts":

                        new MyTask().execute();
                        break;
                    case "NoStore":

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
                SuHelper.showTips("reboot", getString(R.string.Tips_Reboot), this);
                break;
            case R.id.faq:
                Intent intent = new Intent(this, HelperActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
            default:
                return false;
        }
        return true;
    }

    //打开关于
    public void openAbout(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    //打开我的博客
    public void opneHelp(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://blog.coderstory.cn/2016/07/20/mi-kit-feedback/"));
        startActivity(intent);

    }



    //隐藏图标
    private void HideIcon() {
        ComponentName localComponentName = new ComponentName(this,"com.coderstory.miui_toolkit.Activity.SplashActivity");
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.getComponentEnabledSetting(localComponentName);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, "com.coderstory.miui_toolkit.Activity.SplashActivity");
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
    //显示图标
    private void showIcon() {
        ComponentName localComponentName = new ComponentName(this, "com.coderstory.miui_toolkit.Activity.SplashActivity");
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.getComponentEnabledSetting(localComponentName);
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, "com.coderstory.miui_toolkit.Activity.SplashActivity");
        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                PackageManager.DONT_KILL_APP);
    }


    //修改hosts的方法
    private void changeHosts() {
        boolean NoUpdate = prefs.getBoolean("NoUpdate", false); //1
        // boolean RemoveAds = prefs.getBoolean("RemoveAds", false); //2
        boolean GoogleHosts = prefs.getBoolean("GoogleHosts", false); //4
        boolean RemoveAdshosts = prefs.getBoolean("RemoveAdshosts", false); //4
        boolean NoStore = prefs.getBoolean("NoStore", false); //4
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
        if (NoStore) {
            setMap.put("NoStore", "True");
        } else {
            setMap.put("NoStore", "False");
        }
        HostsHelper h = new HostsHelper(MainActivity.this, setMap);

        h.execute();

    }

    //因为hosts修改比较慢 所以改成异步的
    private class MyTask extends AsyncTask<String, Integer, String> {
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
            try {
                Looper.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            changeHosts();
            return null;
        }
    }

    private Dialog dialog;

    private void showProgress() {
        if (dialog == null || ( !dialog.isShowing())) { //dialog未实例化 或者实例化了但没显示
            dialog = ProgressDialog.show(this, getString(R.string.Working), getString(R.string.Waiting));
            dialog.show();
        }
    }

    private void closeProgress() {
        if (!MainActivity.this.isFinishing()) {
            dialog.cancel();
        }
    }
}