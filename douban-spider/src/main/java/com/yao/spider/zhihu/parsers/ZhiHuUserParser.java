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
        List<User> userList = new ArrayList<User>(20);
        //将for循环中要建立的对象在外面先创建，这种做法避免内存中有多份Object对象引用存在，对象很大的话，就耗费内存了
        JSONObject jsonObject = null;
        User user = null;
        //TODO 改为不使用放射机制，效率貌似会比较低
        for (int i = 0,len = jsonArray.size(); i < len; i++) {
            user = new User();
            jsonObject = jsonArray.getJSONObject(i);
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
