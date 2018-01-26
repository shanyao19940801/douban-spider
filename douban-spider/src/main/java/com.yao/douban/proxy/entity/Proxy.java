package com.yao.douban.proxy.entity;

/**
 * Created by 单耀 on 2018/1/26.
 */
public class Proxy {

    private String ip;

    private int port;

    //来源
    private String dataSource;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
