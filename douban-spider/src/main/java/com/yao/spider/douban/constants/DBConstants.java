package com.yao.spider.douban.constants;

/**
 * Created by user on 2018/2/8.
 */
public class DBConstants {
    //获取电影所有热门标签
    public static String MOVE_START_URL_TYPE = "https://movie.douban.com/typerank?type_name=%s&type=%s&interval_id=90:80&action=";
    public static String MOVE_START_TYPE_NAME = "惊悚片";
    public static String MOVE_START_TYPE_VALUE = "10";
    //根据热门标签获取电影列表
    public static String MOVE_TOP_LIST_URL = "https://movie.douban.com/j/chart/top_list?type=%s&interval_id=%d:%d&action=&start=%d&limit=20";

    //根据热门标签获取百分比范围的条数
    public static String MOVE_PERSENT_COUNT_URL = "https://movie.douban.com/j/chart/top_list_count?type=%s&interval_id=%d:%d";
}
