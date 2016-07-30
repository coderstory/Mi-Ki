package com.coderstory.miui_toolkit.tools.Update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.coderstory.miui_toolkit.MainActivity;
import com.coderstory.miui_toolkit.R;
import com.coderstory.miui_toolkit.UpdateServcie;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CoderStory on 2016/7/30.
 */

public class CheckUpdate {

  private  Context mcontext =null;

    public  CheckUpdate(Context context){
        this.mcontext=context;
    }


    /**
     * 检测app的更新信息并保存到UpdateConfig中
     * @throws JSONException
     */
    public static void hasNew() throws JSONException {
        HttpHelper HH=new  HttpHelper();
        String result = HH.RequestUrl(Resources.getSystem().getString(R.string.UpdateServer));
        JSONObject JsonString = new JSONObject(result);//转换为JSONObject
        UpdateConfig.URL = JsonString.getString("URL").toString();
        UpdateConfig.Version = JsonString.getString("Version").toString();
        UpdateConfig.Info = JsonString.getString("info").toString();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            if (!UpdateConfig.Msg.equals("")) {
                Toast.makeText(mcontext, UpdateConfig.Msg, Toast.LENGTH_LONG).show();
            }


            try {
                UpdateConfig.localVersion =mcontext. getPackageManager().getPackageInfo(
                        mcontext.getPackageName(), 0).versionCode; // 设置本地版本号
                UpdateConfig.serverVersion = Integer.parseInt(UpdateConfig.Version);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (UpdateConfig.localVersion < UpdateConfig.serverVersion) {

                // 发现新版本，提示用户更新
                AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
                alert.setTitle("软件升级")
                        .setMessage(UpdateConfig.Info)
                        .setPositiveButton("更新",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 开启更新服务UpdateService
                                        // 这里为了把update更好模块化，可以传一些updateService依赖的值
                                        // 如布局ID，资源ID，动态获取的标题,这里以app_name为例
                                        Intent updateIntent = new Intent(
                                                mcontext,
                                                UpdateServcie.class);
                                        updateIntent.putExtra("titleId",
                                                R.string.app_name);
                                        mcontext.startService(updateIntent);
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                alert.create().show();
            } else {
                // 清理工作，略去
                // cheanUpdateFile()
            }
        }
        // }
    };

    /**
     * 网络操作相关的子线程
     */
   public   Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            // TODO
            // 在这里进行 http request.网络请求相关操作
            try {
                hasNew();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", "OK");
            handler.sendMessage(msg);
        }
    };

}
