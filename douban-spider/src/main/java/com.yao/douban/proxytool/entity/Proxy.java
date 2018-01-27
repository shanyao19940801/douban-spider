package com.yao.douban.proxytool.entity;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by 单耀 on 2018/1/26.
 */
public class Proxy implements Delayed {

    private String ip;

    private int port;

    private long lastSuccessTime;
    //来源
    private String dataSource;

    private long timeIntervsl;//任务间隔时间

    public Proxy(String ip, Integer port, long delayTime, String dataSource) {
        this.ip = ip;
        this.port = port;
        this.timeIntervsl = TimeUnit.NANOSECONDS.convert(delayTime,TimeUnit.MILLISECONDS) + System.nanoTime();
        this.dataSource = dataSource;
    }

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

    public long getLastSuccessTime() {
        return lastSuccessTime;
    }

    public void setLastSuccessTime(long lastSuccessTime) {
        this.lastSuccessTime = lastSuccessTime;
    }

    public long getTimeIntervsl() {
        return timeIntervsl;
    }

    public void setTimeIntervsl(long timeIntervsl) {
        this.timeIntervsl = timeIntervsl;
    }

    public long getDelay(TimeUnit unit) {
        return 0;
    }

    public int compareTo(Delayed o) {
        return 0;
    }
}
