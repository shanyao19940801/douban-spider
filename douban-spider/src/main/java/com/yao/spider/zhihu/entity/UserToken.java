package com.yao.spider.zhihu.entity;

public class UserToken {
    private String userToken;

    public UserToken() {}

    public UserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken == null ? null : userToken.trim();
    }
}