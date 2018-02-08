package com.yao.douban.douban.utils;

import com.yao.douban.douban.DoubanHttpClient;
import com.yao.douban.douban.constants.DBConstants;
import com.yao.douban.proxytool.entity.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018/2/8.
 */
public class DBUtil {
    public static Map<String, String> getType(String type) {
        synchronized (DBUtil.class) {
            Map<String, String> map = new HashMap<String, String>();
            boolean SUCCESS = true;
            while (SUCCESS) {
                if ("move".equals(type)) {
                    try {
                        String url = String.format(DBConstants.MOVE_START_URL_TYPE, URLEncoder.encode(DBConstants.MOVE_START_TYPE_NAME, "UTF-8"), DBConstants.MOVE_START_TYPE_VALUE);
                        Page page = DoubanHttpClient.getInstance().getPage(url);
                        if (page != null && page.getStatusCode() == 200 && !"".equals(page.getHtml())) {
                            return getTypeMap(type, page.getHtml());
                        } else {
                            //TODO 返回错误类型
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    SUCCESS = false;
                }
            }
            return map;
        }
    }

    public static Map getTypeMap(String type, String context) {
        Map<String,String> map = new HashMap<String, String>();
        try {
            if ("move".equals(type)) {
                Document document = Jsoup.parse(context);
                Elements elements = document.select("[class=types]").select("a");
                for (Element element : elements) {
                    String attr = element.attributes().get("href");
                    String[] temp = attr.split("&");
                    String typeName = temp[0].substring(temp[0].indexOf("=") + 1,temp[0].length());
                    String typeValue = temp[1].substring(temp[1].indexOf("=") + 1,temp[1].length());
                    map.put(typeName, typeValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
