package com.coderstory.toolkit.tools;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cc on 2016/6/7.
 */
public class hosts extends SuHelper {
    private Context context = null;
    Map<String,String> Typec;

    public hosts(Context c, Map<String,String> type) {
        this.context = c;
        this.Typec = type;
    }

   // boolean NoUpdate = prefs.getBoolean("NoUpdate", false); //1
   // boolean RemoveAds = prefs.getBoolean("RemoveAds", false); //2
   // boolean GoogleHosts = prefs.getBoolean("GoogleHosts", false); //4
    //setMap.put("RemoveAdshosts", "False");
    @Override
    protected ArrayList<String> getCommandsToExecute() throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<String>();
        list.add("mount -o rw,remount /system");
        Filehelper fh = new Filehelper();
        String content=fh.getFromAssets("none", context);
        list.add("echo '"+content+"' > /system/etc/hosts");//清空

        if (Typec.get("NoUpdate").equals("True")){
             content=fh.getFromAssets("hosts_noup", context);
            list.add("echo '"+content+"' >> /system/etc/hosts");//清空
        }
        if (Typec.get("RemoveAdshosts").equals("True")){
            content=fh.getFromAssets("hosts_noad", context);
            list.add("echo '"+content+"' >> /system/etc/hosts");//清空
        }
        if (Typec.get("GoogleHosts").equals("True")){
            content=fh.getFromAssets("google", context);
            list.add("echo '"+content+"' >> /system/etc/hosts");//清空
        }
        return list;
    }
}
