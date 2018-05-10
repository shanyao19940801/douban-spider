package com.yao.spider.core.entity;

import com.yao.spider.proxytool.entity.Proxy;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shanyao on 2018/5/10.
 */
@Deprecated
public class RequestParams {
    private String url;
    private boolean isUserProxy;
    private AtomicInteger retryTimes;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUserProxy() {
        return isUserProxy;
    }

    public void setUserProxy(boolean userProxy) {
        isUserProxy = userProxy;
    }


    public AtomicInteger getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(AtomicInteger retryTimes) {
        this.retryTimes = retryTimes;
    }
}
