package com.yao.spider.common.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by user on 2018/3/28.
 * 加载配置文件
 */
public class CommonConfig {
    //是否保存到数据库
    public static boolean dbEnable;
    //是否爬取知乎
    public static boolean FUTURE_ZHIHU;
    //是否爬取豆瓣
    public static boolean FUTURE_DOUBAN;
    static {
        Properties p = new Properties();
        try {
            p.load(CommonConfig.class.getResourceAsStream("/config/common.properties"));
            dbEnable = Boolean.parseBoolean(p.getProperty("db.enable"));
            FUTURE_DOUBAN = Boolean.parseBoolean(p.getProperty("future.douban"));
            FUTURE_ZHIHU = Boolean.parseBoolean(p.getProperty("future.zhihu"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
