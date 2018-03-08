package com.yao.douban.douban.entity;

public class Move {
    private String id;

    private String name;

    private String url;

    private String othername;

    private String director;

    private String screenwriter;

    private String mainaactors;

    private String type;

    private String region;

    private String language;

    private String showdate;

    private String runtime;

    private String imdb;

    private Double score;

    private Double votecount;

    private Double evaluationtotal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername == null ? null : othername.trim();
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director == null ? null : director.trim();
    }

    public String getScreenwriter() {
        return screenwriter;
    }

    public void setScreenwriter(String screenwriter) {
        this.screenwriter = screenwriter == null ? null : screenwriter.trim();
    }

    public String getMainaactors() {
        return mainaactors;
    }

    public void setMainaactors(String mainaactors) {
        this.mainaactors = mainaactors == null ? null : mainaactors.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public String getShowdate() {
        return showdate;
    }

    public void setShowdate(String showdate) {
        this.showdate = showdate == null ? null : showdate.trim();
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime == null ? null : runtime.trim();
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb == null ? null : imdb.trim();
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Double getVotecount() {
        return votecount;
    }

    public void setVotecount(Double votecount) {
        this.votecount = votecount;
    }

    public Double getEvaluationtotal() {
        return evaluationtotal;
    }

    public void setEvaluationtotal(Double evaluationtotal) {
        this.evaluationtotal = evaluationtotal;
    }
}