package com.yao.spider.core.http;

/**
 * Created by xuya on 2018/2/13.
 */
public class HttpResponseBean {

    private Integer status;

    private String response;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}