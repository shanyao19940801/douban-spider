package com.yao.spider.core.entity;

import com.yao.spider.proxytool.entity.Proxy;

/**
 * Created by 单耀 on 2018/1/27.
 *
 * 用于封装返回页面信息
 */
public class Page {
    /**
     * 请求页面的url
     */
    private String url;
    /**
     * 请求页面状态吗
     */
    private int statusCode;
    /**
     * 请求页面详细信息
     */
    private String html;
    /**
     * 请求页面代理
     */
    private Proxy proxy;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}
