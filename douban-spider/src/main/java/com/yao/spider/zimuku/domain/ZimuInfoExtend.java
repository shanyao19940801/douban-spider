package com.yao.spider.zimuku.domain;

import java.util.Date;

public class ZimuInfoExtend {
    private Long id;

    private Long zimuInfoId;

    private Integer refType;

    private String extendValue;

    private Integer extendValueType;

    private Integer isDeleted;

    private Date createTime;

    private Date lastUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getZimuInfoId() {
        return zimuInfoId;
    }

    public void setZimuInfoId(Long zimuInfoId) {
        this.zimuInfoId = zimuInfoId;
    }

    public Integer getRefType() {
        return refType;
    }

    public void setRefType(Integer refType) {
        this.refType = refType;
    }

    public String getExtendValue() {
        return extendValue;
    }

    public void setExtendValue(String extendValue) {
        this.extendValue = extendValue;
    }

    public Integer getExtendValueType() {
        return extendValueType;
    }

    public void setExtendValueType(Integer extendValueType) {
        this.extendValueType = extendValueType;
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

    @Override
    public String toString() {
        return "ZimuInfoExtend{" +
                "id=" + id +
                ", zimuInfoId=" + zimuInfoId +
                ", refType=" + refType +
                ", extendValue='" + extendValue + '\'' +
                ", extendValueType=" + extendValueType +
                '}';
    }
}