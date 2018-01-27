package com.yao.douban.proxytool.http.client;

/**
 * Created by 单耀 on 2018/1/27.
 */
public class ProxyHttpClient extends AbstractHttpClient{
    private volatile static ProxyHttpClient instance;

    public static ProxyHttpClient getInstance() {
        if (instance == null) {
            synchronized (ProxyHttpClient.class) {
                if (instance == null) {
                    instance = new ProxyHttpClient();
                }
            }
        }
        return instance;
    }

}
