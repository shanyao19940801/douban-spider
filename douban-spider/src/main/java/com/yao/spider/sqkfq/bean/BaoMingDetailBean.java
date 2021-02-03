package com.yao.spider.sqkfq.bean;

import java.util.Date;

/**
 * @author 单耀
 * @version 1.0
 * @description
 * @date 2021/2/3 10:30
 */
public class BaoMingDetailBean {
    private Integer ZPID;
    private String BT;
    private String BMSJB;
    private String BMSJE;
    private String FKSJB;
    private String FKSJE;
    private String DYSJB;
    private String DYSJE;
    private Integer State;
    private Integer BGM;
    private Date AddTime;
    /**
     * 返回操作状态
     * -2：重新缴费 -1：审核未通过1:我要报名 100：报名审核中  101：网上缴费 102：支付确认中 103：打印准考证 104：打印准考证 1000：报名未开始 1001：报名已结束 2000：缴费未开始 2001：缴费已截止 3000：打印未开始 3001：打印已截止 3002：查询成绩
     */
    private Long OPT;

    public Integer getZPID() {
        return ZPID;
    }

    public void setZPID(Integer ZPID) {
        this.ZPID = ZPID;
    }

    public String getBT() {
        return BT;
    }

    public void setBT(String BT) {
        this.BT = BT;
    }

    public String getBMSJB() {
        return BMSJB;
    }

    public void setBMSJB(String BMSJB) {
        this.BMSJB = BMSJB;
    }

    public String getBMSJE() {
        return BMSJE;
    }

    public void setBMSJE(String BMSJE) {
        this.BMSJE = BMSJE;
    }

    public String getFKSJB() {
        return FKSJB;
    }

    public void setFKSJB(String FKSJB) {
        this.FKSJB = FKSJB;
    }

    public String getFKSJE() {
        return FKSJE;
    }

    public void setFKSJE(String FKSJE) {
        this.FKSJE = FKSJE;
    }

    public String getDYSJB() {
        return DYSJB;
    }

    public void setDYSJB(String DYSJB) {
        this.DYSJB = DYSJB;
    }

    public String getDYSJE() {
        return DYSJE;
    }

    public void setDYSJE(String DYSJE) {
        this.DYSJE = DYSJE;
    }

    public Integer getState() {
        return State;
    }

    public void setState(Integer state) {
        State = state;
    }

    public Integer getBGM() {
        return BGM;
    }

    public void setBGM(Integer BGM) {
        this.BGM = BGM;
    }

    public Date getAddTime() {
        return AddTime;
    }

    public void setAddTime(Date addTime) {
        AddTime = addTime;
    }

    public Long getOPT() {
        return OPT;
    }

    public void setOPT(Long OPT) {
        this.OPT = OPT;
    }
}
