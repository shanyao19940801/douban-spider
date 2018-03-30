package com.yao.spider.zhihu.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by shanyao on 2018/3/28.
 */
public class ZhiHuConfig {
    public static String startURL;
    public static String startUserToken;

//    public final static String USER_FOLLOWEES_URL = "https://www.zhihu.com/api/v4/members/%s/followees?include=data[*].answer_count,articles_count,gender,follower_count,is_followed,is_following,badge[?(type=best_answerer)].topics&offset=%d&limit=20";
/*    public final static String FOLLOWEES_API = "https://www.zhihu.com/api/v4/members/%s/followees?" +
        "include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count," +
        "gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following," +
        "badge[?(type=best_answerer)].topics&offset=%d&limit=20";*/
    public static String FOLLOWEES_API;
    public static String authorization;

    static {
        Properties p = new Properties();
        try {
            p.load(ZhiHuConfig.class.getResourceAsStream("/config/zhihu-config.properties"));
            startURL = p.getProperty("startURL");
            FOLLOWEES_API = p.getProperty("FOLLOWEES_API");
            startUserToken = p.getProperty("startUserToken");
            authorization = p.getProperty("authorization");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

