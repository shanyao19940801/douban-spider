package com.yao.spider.core.util;

/**
 * Created by 单耀 on 2018/1/25.
 */
public class Constants {
    public static final String STANDARD = "standard";
    public static int SocketTimeout = 5000;
    public static int ConnectionTimeout = 5000;
    public static int TIMEOUT = 10000;

    public static String STRTY_URL_MOVE = "https://movie.douban.com/j/search_subjects?type=%s&tag=%s&sort=rank&page_limit=%d&page_start=%d";
    //每次查询信息条数
    public static int LIMIT = 20;
    //查询条件
    public static String TAG = "豆瓣高分";
    //信息类别
    public static String TYPE = "move";
    //是否进行深度爬虫
    public static boolean ISDEEP = false;
    public static boolean ISCONTINUE = true;

}
