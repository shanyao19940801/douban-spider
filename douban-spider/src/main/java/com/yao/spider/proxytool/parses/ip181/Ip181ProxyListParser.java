package com.yao.spider.proxytool.parses.ip181;

import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.core.constants.ProxyConstants;
import jdk.nashorn.internal.parser.JSONParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 单耀 on 2017/12/17.
 */
public class Ip181ProxyListParser implements IPageParser<Proxy> {
    public List<Proxy> parser(String content) {
        List<Proxy> proxyList = null;
        try {
            JSONObject object = JSONObject.fromObject(content);

            String[] encludes = new String[] {"position" };
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setExcludes(encludes);
            JSONArray array = object.getJSONArray("RESULT");
            JSONArray jsonArray = JSONArray.fromObject(array.toString(),jsonConfig);
            proxyList = (List<Proxy>) jsonArray.toCollection(jsonArray, Proxy.class);
        } catch (Exception e) {
        }
        return proxyList;
    }
}
