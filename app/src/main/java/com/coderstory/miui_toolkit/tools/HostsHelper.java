package com.coderstory.miui_toolkit.tools;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 和hosts相关的操作
 * Created by cc on 2016/6/7.
 */
public class HostsHelper extends SuHelper {

    private Context mContext = null; //Context
    Map<String,String> HostsConfig; //hosts的用户配置

    public HostsHelper(Context mContext, Map<String,String> mMap) {
        this.mContext = mContext;
        this.HostsConfig = mMap;
    }
    //构造命令组
    @Override
    protected ArrayList<String> getCommandsToExecute() throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<>();
        list.add("mount -o rw,remount /system");
        FileHelper fh = new FileHelper();
        String content=fh.getFromAssets("none", mContext);
        list.add("echo '"+content+"' > /system/etc/hosts");//清空

        if (HostsConfig.get("NoUpdate").equals("True")){
            content=fh.getFromAssets("hosts_noup", mContext);
            list.add("echo '"+content+"' >> /system/etc/hosts");//禁止检测更新
        }
        if (HostsConfig.get("RemoveAdshosts").equals("True")){
            content=fh.getFromAssets("hosts_noad", mContext);
            list.add("echo '"+content+"' >> /system/etc/hosts");//移除广告
        }
        if (HostsConfig.get("GoogleHosts").equals("True")){
            content=fh.getFromAssets("google", mContext);
            list.add("echo '"+content+"' >> /system/etc/hosts");//谷歌hosts
        }
        if (HostsConfig.get("NoStore").equals("True")){
            content=fh.getFromAssets("hosts_nostore", mContext);
            list.add("echo '"+content+"' >> /system/etc/hosts"); //屏蔽商店 音乐 视频
        }
        return list;
    }
}
