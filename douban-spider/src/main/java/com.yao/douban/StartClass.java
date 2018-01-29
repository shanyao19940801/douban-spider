package com.yao.douban;

import com.yao.douban.proxytool.ProxyHttpClient;

/**
 * Created by 单耀 on 2018/1/24.
 */
public class StartClass {
    public static void main(String[] args) {
        ProxyHttpClient.getInstance().startProxy();
    }
}
