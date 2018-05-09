package com.yao.test.extendtest;

import com.yao.spider.core.http.client.BaseHttpClient;
import com.yao.spider.douban.DoubanHttpClient;

/**
 * Created by shanyao on 2018/5/9.
 */
public class MainFS {
    public static void main(String[] args) {
        SonTest father = new SonTest();
        father.test2();
        BaseHttpClient client1 = BaseHttpClient.getInstance();
        BaseHttpClient client2 = BaseHttpClient.getInstance();
        if (client1 == client2) {
            System.out.println("==");
        }

        System.out.println(BaseHttpClient.getInstance());
        System.out.println(BaseHttpClient.getInstance());
        System.out.println(DoubanHttpClient.getInstance());
        System.out.println(DoubanHttpClient.getInstance());
    }
}
