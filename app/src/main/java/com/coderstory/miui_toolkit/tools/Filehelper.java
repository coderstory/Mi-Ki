package com.coderstory.miui_toolkit.tools;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 文件操作的帮助类
 * Created by cc on 2016/6/7.
 */
public class FileHelper {

    //读取Asssets中的文本文件
    public  String getFromAssets(String FileName, Context mContext){
        try {
            Log.d("TookKit", "getFromAssets: "+FileName);
            InputStreamReader inputReader = new InputStreamReader( mContext.getAssets().open(FileName),"utf-8" );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line+"\n";
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return  "";
        }
    }
}
