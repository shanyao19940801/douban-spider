package com.yao.spider.sqkfq.bean;

import java.util.List;

/**
 * @author 单耀
 * @version 1.0
 * @description
 * @date 2021/2/3 10:29
 */
public class SqjjBean {
    private Integer code;
    private String msg;
    private Integer count;
    private List<BaoMingDetailBean> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<BaoMingDetailBean> getData() {
        return data;
    }

    public void setData(List<BaoMingDetailBean> data) {
        this.data = data;
    }
}
