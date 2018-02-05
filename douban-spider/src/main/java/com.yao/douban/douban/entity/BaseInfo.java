package com.yao.douban.douban.entity;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class BaseInfo {
    //TODO
    private String id;
    private String rate;
    private String title;
    private String url;

    public BaseInfo(String id, String rate, String title, String url) {
        this.id = id;
        this.rate = rate;
        this.title = title;
        this.url = url;
    }
}
