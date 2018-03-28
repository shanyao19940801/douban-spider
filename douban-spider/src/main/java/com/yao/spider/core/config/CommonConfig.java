package com.yao.spider.core.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by user on 2018/3/28.
 * 加载配置文件
 */
public class CommonConfig {
    //是否保存到数据库
    public static boolean dbEnable;

    static {
        Properties p = new Properties();
        try {
            p.load(CommonConfig.class.getResourceAsStream("/config/commonconfig.properties"));
            dbEnable = Boolean.parseBoolean(p.getProperty("db.enable"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
