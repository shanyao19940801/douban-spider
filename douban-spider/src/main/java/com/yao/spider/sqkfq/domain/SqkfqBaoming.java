package com.yao.spider.sqkfq.domain;

import java.util.Date;

public class SqkfqBaoming {
    private Long id;

    private Long userMid;

    private Integer zpid;

    private String title;

    private Date startAppyTime;

    private Date endAppyTime;

    private Date startPayTime;

    private Date endPayTime;

    private Date startPrintTime;

    private Date endPrintTime;

    private Integer state;

    private Integer bgm;

    private Date addTime;

    private Long opt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserMid() {
        return userMid;
    }

    public void setUserMid(Long userMid) {
        this.userMid = userMid;
    }

    public Integer getZpid() {
        return zpid;
    }

    public void setZpid(Integer zpid) {
        this.zpid = zpid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartAppyTime() {
        return startAppyTime;
    }

    public void setStartAppyTime(Date startAppyTime) {
        this.startAppyTime = startAppyTime;
    }

    public Date getEndAppyTime() {
        return endAppyTime;
    }

    public void setEndAppyTime(Date endAppyTime) {
        this.endAppyTime = endAppyTime;
    }

    public Date getStartPayTime() {
        return startPayTime;
    }

    public void setStartPayTime(Date startPayTime) {
        this.startPayTime = startPayTime;
    }

    public Date getEndPayTime() {
        return endPayTime;
    }

    public void setEndPayTime(Date endPayTime) {
        this.endPayTime = endPayTime;
    }

    public Date getStartPrintTime() {
        return startPrintTime;
    }

    public void setStartPrintTime(Date startPrintTime) {
        this.startPrintTime = startPrintTime;
    }

    public Date getEndPrintTime() {
        return endPrintTime;
    }

    public void setEndPrintTime(Date endPrintTime) {
        this.endPrintTime = endPrintTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getBgm() {
        return bgm;
    }

    public void setBgm(Integer bgm) {
        this.bgm = bgm;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Long getOpt() {
        return opt;
    }

    public void setOpt(Long opt) {
        this.opt = opt;
    }
}