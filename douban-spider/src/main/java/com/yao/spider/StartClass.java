package com.yao.spider;

import com.yao.spider.common.config.CommonConfig;
import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.proxytool.ProxyHttpClient;
import com.yao.spider.zhihu.ZhiHuHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 单耀 on 2018/1/24.
 */
public class StartClass {
    private static Logger logger = LoggerFactory.getLogger(StartClass.class);
    public static void main(String[] args) {
        ProxyHttpClient.getInstance().startProxy();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (CommonConfig.FUTURE_DOUBAN) {
            DoubanHttpClient.getInstance().startDouBan();
        }

        if (CommonConfig.FUTURE_ZHIHU) {
            ZhiHuHttpClient.getInstance().startZhiHu();
        }
    }
}
