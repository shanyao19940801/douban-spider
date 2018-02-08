package com.yao.douban;

import com.yao.douban.douban.DoubanHttpClient;
import com.yao.douban.proxytool.ProxyHttpClient;

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
