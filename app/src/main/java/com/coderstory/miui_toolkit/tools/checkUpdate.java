package com.coderstory.miui_toolkit.tools;

import com.coderstory.miui_toolkit.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * app更新
 */

public class checkUpdate {

    /**
     * 调用接口
     *
     * @param wsurl
     * @return
     */

    public static String RequestUrl(String wsurl) {
        String rvalue = "";
        try {
            //声明URL
            URL url = new URL(wsurl);
            //打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置连接方式
            conn.setRequestMethod("POST");
            //设置是否输入参数

            //获取返回值
            InputStream inStream = conn.getInputStream();
            //流转化为字符串
            rvalue = streamToStr(inStream);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rvalue;
    }

    /**
     * 把流对象转换成字符串对象
     *
     * @param is
     * @return
     */
    public static String streamToStr(InputStream is) {
        try {
            // 定义字节数组输出流对象
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 定义读取的长度
            int len = 0;
            // 定义读取的缓冲区
            byte buffer[] = new byte[1024];
            // 按照定义的缓冲区进行循环读取，直到读取完毕为止
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到字节数组输出流对象中
                os.write(buffer, 0, len);
            }
            // 关闭流
            is.close();
            os.close();
            // 把读取的字节数组输出流对象转换成字节数组
            byte data[] = os.toByteArray();
            // 按照指定的编码进行转换成字符串(此编码要与服务端的编码一致就不会出现乱码问题了，android默认的编码为UTF-8)
            return new String(data, "UTF-8");
        } catch (IOException e) {
            config.Msg="更新服务器访问失败，请稍后再试。。。";
            e.printStackTrace();
            return null;
        }
    }


    public static void checkUpdate2() throws JSONException {
        String result = RequestUrl("http://coderstory.picp.io/info");
        JSONObject jsonstring = new JSONObject(result);//转换为JSONObject
        config.URL = jsonstring.getString("URL").toString();
        config.Version = jsonstring.getString("Version").toString();
        config.Info = jsonstring.getString("info").toString();
    }


}
