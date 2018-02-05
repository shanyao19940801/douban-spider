package com.yao.douban.douban.entity.move;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class Move {
    private String id;
    private String name;
    private String otherName;//又名
    private String director;//导演
    private String screenWriter;//编剧
    private String mainaActors;//主演
    private String type;//类型
    private String flakingArea;//制片国家、地区
    private String language;//语言
    private String showDate;//上映日期
    private String runTime;//片长
    private String imdb;//IMDb链接
    private Double rating;//豆瓣评分
    private Integer ratingSum;//评价人数
    private Integer evaluationTotal;//评价总数

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getScreenWriter() {
        return screenWriter;
    }

    public void setScreenWriter(String screenWriter) {
        this.screenWriter = screenWriter;
    }

    public String getMainaActors() {
        return mainaActors;
    }

    public void setMainaActors(String mainaActors) {
        this.mainaActors = mainaActors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFlakingArea() {
        return flakingArea;
    }

    public void setFlakingArea(String flakingArea) {
        this.flakingArea = flakingArea;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(Integer ratingSum) {
        this.ratingSum = ratingSum;
    }

    public Integer getEvaluationTotal() {
        return evaluationTotal;
    }

    public void setEvaluationTotal(Integer evaluationTotal) {
        this.evaluationTotal = evaluationTotal;
    }

    @Override
    public String toString() {
        return "Move{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", otherName='" + otherName + '\'' +
                ", director='" + director + '\'' +
                ", screenWriter='" + screenWriter + '\'' +
                ", mainaActors='" + mainaActors + '\'' +
                ", type='" + type + '\'' +
                ", flakingArea='" + flakingArea + '\'' +
                ", language='" + language + '\'' +
                ", showDate='" + showDate + '\'' +
                ", runTime='" + runTime + '\'' +
                ", imdb='" + imdb + '\'' +
                ", rating=" + rating +
                ", ratingSum=" + ratingSum +
                ", evaluationTotal=" + evaluationTotal +
                '}';
    }
}
