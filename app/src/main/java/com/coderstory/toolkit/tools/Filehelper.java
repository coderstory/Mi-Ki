package com.coderstory.toolkit.tools;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by cc on 2016/6/7.
 */
public class Filehelper {
    public  String getFromAssets(String fileName, Context context){
        try {
            InputStreamReader inputReader = new InputStreamReader( context.getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return  "";
        }
    }
}
