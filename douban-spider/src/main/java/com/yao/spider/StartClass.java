package com.yao.spider;

import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.proxytool.ProxyHttpClient;

/**
 * Created by 单耀 on 2018/1/24.
 */
public class StartClass {
    public static void main(String[] args) {

        ProxyHttpClient.getInstance().startProxy();
//        DoubanHttpClient.getInstance().startDouBan();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DoubanHttpClient.getInstance().startDouBan();

    }
}
