package com.coderstory.toolkit.tools;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by cc on 2016/6/7.
 */
public class Filehelper {
    public  String getFromAssets(String fileName, Context context){
        try {
            Log.d("tookit", "getFromAssets: "+fileName);
            InputStreamReader inputReader = new InputStreamReader( context.getAssets().open(fileName),"utf-8" );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
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
