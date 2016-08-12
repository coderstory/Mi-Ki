package com.coderstory.miui_toolkit.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.coderstory.miui_toolkit.Adapter.AppInfo;
import com.coderstory.miui_toolkit.Adapter.AppInfoAdapter;
import com.coderstory.miui_toolkit.R;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DisableAppActivity extends AppCompatActivity {


    private List<AppInfo> appInfoList = new ArrayList<>();
    private List<AppInfo> appInfoList2 = new ArrayList<>();
    // private Context mContext=null;
    List<PackageInfo> packages=new ArrayList<>();
    AppInfoAdapter adapter=null;
    ListView listView=null;
    AppInfo appInfo=null ;
    int  mposition=0;
    View mview=null;
    com.yalantis.phoenix.PullToRefreshView mPullToRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_app);
        // mContext = DisableAppActivity.this;
        Toast.makeText(this, R.string.disableapptips,Toast.LENGTH_LONG).show();



        new MyTask().execute();

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        showData();
                        adapter.notifyDataSetChanged();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }

    private void initFruit() {
        //packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0 表示是系统应用
        appInfoList.clear();
        appInfoList2.clear();
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                if (packageInfo.applicationInfo.enabled) {
                    AppInfo appInfo = new AppInfo(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getPackageManager()), packageInfo.packageName, false, packageInfo.applicationInfo.sourceDir, String.valueOf(packageInfo.versionName));
                    appInfoList.add(appInfo);
                } else {
                    AppInfo appInfo = new AppInfo(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString(), packageInfo.applicationInfo.loadIcon(getPackageManager()), packageInfo.packageName, true, packageInfo.applicationInfo.sourceDir, String.valueOf(packageInfo.versionName));
                    appInfoList2.add(appInfo);
                }
            }
        }
        appInfoList.addAll(appInfoList2);
    }

    private void initData()
    {
        packages = new ArrayList<>();
        packages  = getPackageManager().getInstalledPackages(0);
        initFruit();
    }

    private  void  showData()
    {
        adapter = new AppInfoAdapter(this, R.layout.app_info_item, R.color.disableApp,appInfoList);
        listView = (ListView) findViewById(R.id.listView);
        assert listView != null;
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mposition = position;
                mview=view;
                AlertDialog.Builder dialog = new AlertDialog.Builder(DisableAppActivity.this);
                dialog.setTitle(R.string.Tips_Title);
                String tipsText;
                String BtnText=getString(R.string.Btn_Sure) ;
                appInfo = appInfoList.get(mposition);
                if (appInfo.getDisable()) {
                    tipsText = getString(R.string.sureAntiDisable)+appInfo.getName()+getString(R.string.sureAntiDisableAfter);
                }else {
                    tipsText = getString(R.string.sureDisable)+appInfo.getName() +getString(R.string.sureDisableAfter);
                }
                dialog.setMessage(tipsText);
                dialog.setPositiveButton(BtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String commandText = "pm disable " + appInfo.getPackageName();
                        Log.e("cc", commandText);
                        Process process = null;
                        DataOutputStream os = null;
                        try {
                            if (appInfo.getDisable() ) {
                                commandText = "pm enable " + appInfo.getPackageName();
                            }
                            String cmd = commandText;
                            process = Runtime.getRuntime().exec("su"); //切换到root帐号
                            os = new DataOutputStream(process.getOutputStream());
                            os.writeBytes(cmd + "\n");
                            os.writeBytes("exit\n");
                            os.flush();
                            process.waitFor();
                            // View rootView = LayoutInflater.from(mContext).inflate(R.layout.app_info_item, null);
                            if (appInfo.getDisable()) {
                                appInfo.setDisable(false);
                                appInfoList.set(mposition, appInfo);
                                mview.setBackgroundColor(Color.parseColor("#d027b6d2")); //正常的颜色
                            } else {
                                appInfo.setDisable(true);
                                appInfoList.set(mposition, appInfo);
                                mview.setBackgroundColor(Color.parseColor("#d0d7d7d7")); //冻结的颜色

                            }
                        } catch (Exception ignored) {

                        } finally {
                            try {
                                if (os != null) {
                                    os.close();
                                }
                                assert process != null;
                                process.destroy();
                            } catch (Exception ignored) {
                            }
                        }
                    }
                });
                dialog.setCancelable(true);
                dialog.setNegativeButton(R.string.Btn_Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }


    class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
            showProgress();
        }

        @Override
        protected void onPostExecute(String param) {
            showData();
            setProgressBarIndeterminateVisibility(false);
            adapter.notifyDataSetChanged();
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
            initData();
            return null;
        }
    }

    private Dialog dialog;
    protected void showProgress() {
        if(dialog == null) {
//		    dialog.setContentView(R.layout.progress_dialog);
            //    dialog.getWindow().setAttributes(params);
            dialog = ProgressDialog.show(this,getString( R.string.Tips_Title), getString(R.string.loadappinfo));
            dialog.show();
        }
    }
    //
    protected void closeProgress() {

        if(dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }
}
