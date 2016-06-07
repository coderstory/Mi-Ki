package com.coderstory.toolkit.tools;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by cc on 2016/6/7.
 */
public class hosts extends SuHelper {
    private Context context = null;
    String Type;

    public hosts(Context c, String type) {
        this.context = c;
        this.Type = type;
    }

    @Override
    protected ArrayList<String> getCommandsToExecute() {
        ArrayList<String> list = new ArrayList<String>();
        String Path = "";
        switch (Type) {
            case "None":
                Path = "hosts_NONE";
                break;
            case "NoAD":
                Path = "hosts_NOAD";
                break;
            case "NOUP":
                Path = "hosts_NOUP";
                break;
            case "NOAD_NOUP":
                Path = "NOAD_NOUP_NOUP";
                break;
        }
        Filehelper fh = new Filehelper();
        String content = fh.getFromAssets(Path, context);
        if (content.equals("")) {
            Log.e("toolkit", "ChangeHosts:hosts文件读取失败 ");
        }
        list.add("mount -o rw,remount /system");
        list.add("echo " + content + " > /system/etc/hosts");
        return list;
    }
}
