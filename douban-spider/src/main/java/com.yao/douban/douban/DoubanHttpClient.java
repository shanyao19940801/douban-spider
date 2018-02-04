package com.yao.douban.douban;

import com.yao.douban.proxytool.http.client.AbstractHttpClient;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class DoubanHttpClient extends AbstractHttpClient{
    private static DoubanHttpClient instance;

    public static DoubanHttpClient getInstance() {
        if (instance == null) {
            synchronized (DoubanHttpClient.class) {
                if (instance == null) {
                    instance = new DoubanHttpClient();
                }
            }
        }
        return instance;
    }
}
