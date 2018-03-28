package com.yao.spider.proxytool.entity;

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by 单耀 on 2018/1/26.
 * 在一些字段前面加上 transient关键字，目的是为了序列化时忽略该字段，因为序列化时这些null的字段会占用不必要的空间
 * 可以查看ArrayList源码中就是采用这种技术避免null被序列化
 *
 */
public class Proxy implements Delayed,Serializable {

    private static final long serialVersionUID = -3231293936247728930L;

    private String ip;
    private Integer port;
    private long lastSuccessTime;
    //来源
    private String dataSource;
    transient private long timeIntervsl;//任务间隔时间
    transient private int failureTimes;//请求失败次数
    transient private int successfulTimes;//请求成功次数

    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

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

    public int getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(int failureTimes) {
        this.failureTimes = failureTimes;
    }

    public int getSuccessfulTimes() {
        return successfulTimes;
    }

    public void setSuccessfulTimes(int successfulTimes) {
        this.successfulTimes = successfulTimes;
    }

    public String getProxyStr() {
        return ip + ":" + port;
    }

}
