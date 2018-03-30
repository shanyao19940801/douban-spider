package com.yao.spider.zhihu.parsers;

import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.zhihu.entity.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/3/28.
 */
public class ZhiHuUserParser implements IPageParser<User>{
    public List<User> parser(String html) {
        JSONObject object = JSONObject.fromObject(html);
        JSONArray jsonArray = object.getJSONArray("data");
        List<User> userList = new ArrayList<User>();
        for (int i = 0; i < jsonArray.size(); i++) {
            User user = new User();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            setUserPropertyValue(user, "agrees", jsonObject, "voteup_count");
            setUserPropertyValue(user, "answers", jsonObject, "answer_count");
            setUserPropertyValue(user, "asks", jsonObject, "question_count");
            setUserPropertyValue(user, "username", jsonObject, "name");
            setUserPropertyValue(user, "business", jsonObject.getJSONObject("business"), "business");
            JSONArray educations = jsonObject.getJSONArray("educations");

            if (educations != null && educations.size() > 1) {
                setUserPropertyValue(user, "education", educations.getJSONObject(0).getJSONObject("school"), "name");
            }
            JSONArray employments = jsonObject.getJSONArray("employments");
            if (employments != null && employments.size() > 0) {
                setUserPropertyValue(user, "position", employments.getJSONObject(0).getJSONObject("job"), "name");
                setUserPropertyValue(user, "company", employments.getJSONObject(0).getJSONObject("company"), "name");
            }
            setUserPropertyValue(user, "followees", jsonObject, "following_count");
            setUserPropertyValue(user, "followers", jsonObject, "follower_count");
            JSONArray locations = jsonObject.getJSONArray("locations");
            if (educations != null && educations.size() > 1) {
                setUserPropertyValue(user, "location", locations.getJSONObject(0), "name");
            }
            setUserPropertyValue(user, "articles", jsonObject, "articles_count");
            setUserPropertyValue(user, "thanks", jsonObject, "voteup_count");
            setUserPropertyValue(user, "url", jsonObject, "url");
            setUserPropertyValue(user, "userToken", jsonObject, "url_token");
            setUserPropertyValue(user, "userId", jsonObject, "id");

            /*Integer voteup_count =  jsonObject.getInt("voteup_count");
            user.setAgrees(voteup_count);
            Integer answer_count =  jsonObject.getInt("answer_count");
            user.setAnswers(answer_count);
            Integer question_count =  jsonObject.getInt("question_count");
            user.setAsks(question_count);
            String name =  jsonObject.getString("name");
            user.setUsername(name);
            JSONObject business = jsonObject.getJSONObject("business");
            String industry = business.getString("name");
            user.setBusiness(industry);
            JSONArray educations = jsonObject.getJSONArray("educations");
            if (educations != null && educations.size() > 1) {
                String school = educations.getJSONObject(0).getJSONObject("school").getString("name");
                user.setEducation(school);
            }
            JSONArray employments = jsonObject.getJSONArray("employments");
            if (employments != null && employments.size() > 0) {
                JSONObject job = employments.getJSONObject(0).getJSONObject("job");
                user.setPosition(job.getString("name"));
                JSONObject company = employments.getJSONObject(0).getJSONObject("company");
                user.setCompany(company.getString("name"));
            }
            Integer followwws =  jsonObject.getInt("following_count");
            user.setFollowees(followwws);
            Integer follower_count =  jsonObject.getInt("follower_count");
            user.setFollowers(follower_count);
            JSONArray locations = jsonObject.getJSONArray("locations");
            if (educations != null && educations.size() > 1) {
                String location = locations.getJSONObject(0).getString("name");
                user.setLocation(location);
            }
            Integer articles_count =  jsonObject.getInt("articles_count");
            user.setArticles(articles_count);
            Integer thanked_count =  jsonObject.getInt("voteup_count");
            user.setThanks(thanked_count);
            String url =  jsonObject.getString("url");
            user.setUrl(url);
            String url_token =  jsonObject.getString("url_token");
            user.setUserToken(url_token);
            String id =  jsonObject.getString("id");
            user.setUserId(id);*/

            //gender
            Integer gender =  jsonObject.getInt("gender");
            if (gender != null) {
                if (gender == 1) {
                    user.setSex("男");
                } else {
                    user.setSex("女");
                }
            }
            userList.add(user);

        }
        return userList;
    }
    //

    /**
     *  通过反射技术对User属性复制
     * @param user          User实体
     * @param propertyName 要赋值的属性名
     * @param jsonObject   JSON对象
     * @param key           从JSON中对应的属性名称
     *  学习参考网址：
     *    1. https://blog.csdn.net/starryninglong/article/details/60468440
     *    2. https://www.cnblogs.com/zhouyalei/archive/2013/09/12/java-reflect.html
     */
    private void setUserPropertyValue(User user, String propertyName, JSONObject jsonObject, String key) {
        try {
            Field field = user.getClass().getDeclaredField(propertyName);
            Object o = jsonObject.get(key);
            field.setAccessible(true);
            field.set(user, o);
        } catch (Exception e) {

        }
    }
}
