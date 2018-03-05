package com.yao.douban.douban.entity.move;

import com.yao.douban.douban.entity.BaseInfo;

/**
 * Created by 单耀 on 2018/2/2.
 */
public class Move {

    private String id;
    private Double rate;
    private String title;
    private String url;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Move{" +
                "id='" + id + '\'' +
                ", rate='" + rate + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
