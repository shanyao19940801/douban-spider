package com.yao.douban.douban.entity.move;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/30.
 *

 "id ": "1889243 ",
 "types ":[ "剧情 ",
 , "regions ":[ "美国 ", "英国 ", "加拿大 ", "冰岛 "]
 , "title ": "星际穿越 "
 , "url ": "https:  /  /movie.douban.com  /subject  /1889243  / "
 , "release_date ": "2014-11-12 "
 , "vote_count ":530258
 , "score ": "9.2 "
 , "actors ":[ "马修·麦康纳 ", "安妮·海瑟薇 ", "杰西卡·查斯坦 ", "卡西·阿弗莱克 ", "迈克尔·凯恩 ", "马特·达蒙 ", "麦肯吉·弗依 ", "蒂莫西·柴勒梅德 ", "艾伦·伯斯汀 ", "约翰·利思戈 ", "韦斯·本特利 ", "大卫·吉雅西 ", "比尔·欧文 ", "托弗·戈瑞斯 ", "科莱特·沃夫 ", "弗朗西斯·X·麦卡蒂 ", "安德鲁·博尔巴 ", "乔什·斯图沃特 ", "莱雅·卡里恩斯 ", "利亚姆·迪金森 ", "杰夫·赫普内尔 ", "伊莱耶斯·加贝尔 ", "布鲁克·史密斯 ", "大卫·奥伊罗 ", "威廉姆·德瓦内 ", "拉什·费加 ", "格里芬·弗雷泽 ", "弗洛拉·诺兰 "]
 */
public class Move {
    private String id;
    private String name;    //title
    private String url;
    private String othername;//又名
    private String director;//导演
    private String screenwriter;//编剧
    private String mainaactors;//主演 actors
    private String type;//类型 types
    private String region;//制片国家、地区
    private String language;//语言
    private String showdate;//上映日期  release_date
    private String runtime;//片长
    private String imdb;//IMDb链接
    private Double score;//豆瓣评分
    private Integer votecount;//评价人数 vote_count
    private Integer evaluationtotal;//评价总数

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getScreenwriter() {
        return screenwriter;
    }

    public void setScreenwriter(String screenwriter) {
        this.screenwriter = screenwriter;
    }

    public String getMainaactors() {
        return mainaactors;
    }

    public void setMainaactors(String mainaactors) {
        this.mainaactors = mainaactors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getShowdate() {
        return showdate;
    }

    public void setShowdate(String showdate) {
        this.showdate = showdate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getImdb() {
        return imdb;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getVotecount() {
        return votecount;
    }

    public void setVotecount(Integer votecount) {
        this.votecount = votecount;
    }

    public Integer getEvaluationtotal() {
        return evaluationtotal;
    }

    public void setEvaluationtotal(Integer evaluationtotal) {
        this.evaluationtotal = evaluationtotal;
    }

    public void setTypes(List<String> types) {
        StringBuilder type = new StringBuilder();
        for (String temp : types) {
            type.append(temp).append("\\");
        }
        type.deleteCharAt(type.length()-1);
        this.type = type.toString();
    }

    //下面的set方法用于json转换时自U单名不一样时保证成功转换
    public void setRegions(List<String> regions) {
        StringBuilder region = new StringBuilder();
        for (String temp : regions) {
            region.append(temp).append("\\");
        }
        region.deleteCharAt(region.length()-1);
        this.region = region.toString();
    }
    public void setActors(List<String> actors) {
        StringBuilder actor = new StringBuilder();
        int i = 0;
        for (String temp : actors) {
            actor.append(temp);
            if (++i > 5) {
                break;
            }
            actor.append("\\");
        }
        this.mainaactors = actor.toString();
    }

    /*public void setRelease_date(List<String> actors) {
        StringBuilder actor = new StringBuilder();
        int i = 0;
        for (String temp : actors) {
            actor.append(temp);
            if (++i > 5) {
                break;
            }
            actor.append("\\");
        }
        this.mainaactors = actor.toString();
    }*/

    public void setTitle(String title){
        this.name = title;
    }
    public void setRelease_date(String release_date) {
        this.showdate = release_date;
    }
    public void setVote_count(Integer vote_count) {
        this.votecount = vote_count;
    }



    @Override
    public String toString() {
        return "Move{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", othername='" + othername + '\'' +
                ", director='" + director + '\'' +
                ", screenwriter='" + screenwriter + '\'' +
                ", mainaactors='" + mainaactors + '\'' +
                ", type='" + type + '\'' +
                ", region='" + region + '\'' +
                ", language='" + language + '\'' +
                ", showdate='" + showdate + '\'' +
                ", runtime='" + runtime + '\'' +
                ", imdb='" + imdb + '\'' +
                ", score=" + score +
                ", votecount=" + votecount +
                ", evaluationtotal=" + evaluationtotal +
                '}';
    }
}
