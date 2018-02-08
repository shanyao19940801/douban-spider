package com.yao.test.jsontest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by user on 2018/2/8.
 */
public class JsonTest {
    public static void main(String[] args) {
        String context = "{\"playable_count\":320,\"total\":554,\"unwatched_count\":554}";
//        JSONArray array = JSONArray.fromObject(context);
        JSONObject object = JSONObject.fromObject(context);
        System.out.println(object);
    }
}
