package com.coderstory.miui_toolkit.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.coderstory.miui_toolkit.R;
import com.umeng.analytics.MobclickAgent;


public class SplashActivity extends AppCompatActivity {
    private static final int SHOW_TIME_MIN = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //倒计时返回主界面
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPostExecute(Integer result) {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            protected Integer doInBackground(Void... params) {
                long startTime = System.currentTimeMillis();
                long loadingTime = System.currentTimeMillis() - startTime;
                if (loadingTime < SHOW_TIME_MIN) {
                    try {
                        Thread.sleep(SHOW_TIME_MIN - loadingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return 1;
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
