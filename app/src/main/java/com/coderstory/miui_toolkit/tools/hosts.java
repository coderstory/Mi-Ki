package com.coderstory.miui_toolkit.tools;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

/**
 * 和hosts相关的操作
 * Created by cc on 2016/6/7.
 */
public class hosts extends SuHelper {

    private Context context = null; //Context
    Map<String,String> Typec; //hosts的用户配置

    public hosts(Context c, Map<String,String> type) {
        this.context = c;
        this.Typec = type;
    }
    //构造命令组
    @Override
    protected ArrayList<String> getCommandsToExecute() throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<>();
        list.add("mount -o rw,remount /system");
        Filehelper fh = new Filehelper();
        String content=fh.getFromAssets("none", context);
        list.add("echo '"+content+"' > /system/etc/hosts");//清空

        if (Typec.get("NoUpdate").equals("True")){
             content=fh.getFromAssets("hosts_noup", context);
            list.add("echo '"+content+"' >> /system/etc/hosts");//禁止检测更新
        }
        if (Typec.get("RemoveAdshosts").equals("True")){
            content=fh.getFromAssets("hosts_noad", context);
            list.add("echo '"+content+"' >> /system/etc/hosts");//移除广告
        }
        if (Typec.get("GoogleHosts").equals("True")){
            content=fh.getFromAssets("google", context);
            list.add("echo '"+content+"' >> /system/etc/hosts");//谷歌hosts
        }
        return list;
    }
}
