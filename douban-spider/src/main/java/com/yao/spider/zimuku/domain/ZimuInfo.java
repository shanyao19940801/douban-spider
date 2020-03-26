package com.yao.spider.zimuku.domain;

import java.util.Date;

public class ZimuInfo {
    private Long id;

    private Long zimuId;

    private String zimuTitle;

    private String zimuTranslator;

    private Integer zimuLanguage;

    private Float zimuQuality;

    private Integer zimuType;

    private String detailUrl;

    private String downloadPageUrl;

    private Integer isDeleted;

    private Date createTime;

    private Date lastUpdateTime;

    private Long subId;

    private String subName;

    private Long htmlId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getZimuId() {
        return zimuId;
    }

    public void setZimuId(Long zimuId) {
        this.zimuId = zimuId;
    }

    public String getZimuTitle() {
        return zimuTitle;
    }

    public void setZimuTitle(String zimuTitle) {
        this.zimuTitle = zimuTitle;
    }

    public String getZimuTranslator() {
        return zimuTranslator;
    }

    public void setZimuTranslator(String zimuTranslator) {
        this.zimuTranslator = zimuTranslator;
    }

    public Integer getZimuLanguage() {
        return zimuLanguage;
    }

    public void setZimuLanguage(Integer zimuLanguage) {
        this.zimuLanguage = zimuLanguage;
    }

    public Float getZimuQuality() {
        return zimuQuality;
    }

    public void setZimuQuality(Float zimuQuality) {
        this.zimuQuality = zimuQuality;
    }

    public Integer getZimuType() {
        return zimuType;
    }

    public void setZimuType(Integer zimuType) {
        this.zimuType = zimuType;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getDownloadPageUrl() {
        return downloadPageUrl;
    }

    public void setDownloadPageUrl(String downloadPageUrl) {
        this.downloadPageUrl = downloadPageUrl;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public Long getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(Long htmlId) {
        this.htmlId = htmlId;
    }
}