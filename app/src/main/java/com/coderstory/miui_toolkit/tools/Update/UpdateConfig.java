package com.coderstory.miui_toolkit.tools.Update;
/**
 * APP在线功能的检测配置信息
 */

public  class   UpdateConfig {
    //版本信息
    public static int localVersion = 0;
    public static int serverVersion = 0;
    /* 下载包安装路径 */
    public static final String saveFileName =  "/Mi Kit/" ;
    public static String  URL="";//app的下载地址
    public static String Version="";//最新版的versionCode
    public static  String Info=""; //更新内容
    public  static String errorMsg =""; //错误提示
    public static String  UpdateServer="http://blog.coderstory.cn/info";
}
