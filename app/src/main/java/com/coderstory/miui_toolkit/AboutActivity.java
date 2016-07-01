package com.coderstory.miui_toolkit;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    //打开我的博客
    public void opneUrl(View view) {
        //  Toast.makeText(this, "",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://blog.coderstory.cn"));
        startActivity(intent);

    }
    //打开支付宝
    public void alipay(View view) {
        //  Toast.makeText(this, "",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://qr.alipay.com/aex087445gnaa6gawjaohe8"));
        startActivity(intent);
    }

}
