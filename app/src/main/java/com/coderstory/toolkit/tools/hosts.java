package com.coderstory.toolkit.tools;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by cc on 2016/6/7.
 */
public class hosts extends SuHelper {
    private Context context = null;
    String Typec;

    public hosts(Context c, String type) {
        this.context = c;
        this.Typec = type;
    }

    @Override
    protected ArrayList<String> getCommandsToExecute() throws UnsupportedEncodingException {
        ArrayList<String> list = new ArrayList<String>();

        Filehelper fh = new Filehelper();
        String content = fh.getFromAssets(Typec, context);
        if (content.equals("")) {
            Log.e("toolkit", "ChangeHosts:hosts文件读取失败 ");
        }
        Log.d("tookit", "getCommandsToExecute: "+content);
        list.add("mount -o rw,remount /system");
        list.add("echo '" +content   + "' > /system/etc/hosts");
        return list;
    }
}
