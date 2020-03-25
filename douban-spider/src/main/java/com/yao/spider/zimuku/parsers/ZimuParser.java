package com.yao.spider.zimuku.parsers;

import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.zimuku.domain.ZimuInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/3/28.
 */
public class ZimuParser implements IPageParser<ZimuInfo> {
    private static final Logger logger = LoggerFactory.getLogger(ZimuParser.class);
    private static final String host = "http://www.zimuku.la/";

    public List<ZimuInfo> parser(String html) {
        Document document = Jsoup.parse(html);
        Elements oddList = document.select(".odd");
        Elements evenList = document.select(".even");
        List<ZimuInfo> list = new ArrayList<ZimuInfo>(30);
        for (Element element : oddList) {
            try {
                list.add(buildBean(element));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Element element : evenList) {
            try {
                list.add(buildBean(element));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private static ZimuInfo buildBean( Element element) {
        ZimuInfo info = new ZimuInfo();
        Elements select = element.select("a");
        fillSubInfo(info, select);

        Element detail = select.get(1);
        info.setZimuId(Long.valueOf(getZimuId(detail)));
        info.setZimuTitle(detail.attr("title"));
        info.setDetailUrl(host + detail.attr("href"));
        info.setZimuType(1);
        info.setZimuTranslator(getTranslator(element));
        info.setZimuQuality(zimuQuality(element));
        info.setZimuLanguage(langInt(element));
        return info;
    }

    private static void fillSubInfo(ZimuInfo info, Elements select) {
        try {
            Element subElement = select.get(0);
            info.setSubId(getSubId(subElement));
            info.setSubName(subElement.attr("title"));
        } catch (NumberFormatException e) {
            logger.warn("no sub info");
        }
    }

    private static String getTranslator(Element element) {
        Elements select = element.select(".label").select(".label-danger");
        return select.get(0).childNode(0).attr("text");
    }

    private static String getZimuId( Element subElement) {
        String subId = subElement.attr("href");
        int startIndex = subId.lastIndexOf("/");
        int endIndex = subId.indexOf(".");
        String id = subId.substring(startIndex + 1, endIndex);
        return id;
    }

    private static Long getSubId( Element subElement) {
        String subId = subElement.attr("href");
        if (StringUtils.isEmpty(subId)) {
            return null;
        }
        int startIndex = subId.lastIndexOf("/");
        int endIndex = subId.indexOf(".");
        String id = subId.substring(startIndex + 1, endIndex);
        return Long.valueOf(id);
    }

    private static float zimuQuality(Element elements) {
        Elements select = elements.select(".tac").select(".hidden-xs");
        Element element = select.get(0);
        String title = element.child(0).attr("title");
        int start = title.indexOf(":");
        return Float.valueOf(title.substring(start + 1, title.length() - 1));
    }

    private static int langInt(Element element) {
        Elements lang = element.select(".tac").select(".lang");
        Elements langImgs = lang.select("img");
        int sum = 0;
        for (Element lan : langImgs) {
            if ("简体中文字幕".equals(lan.attr("title"))) {
                sum += 1 << 3;
            }
            if ("繁體中文字幕".equals(lan.attr("title"))) {
                sum += 1 << 1;
            }
            if ("English字幕".equals(lan.attr("title"))) {
                sum += 1 << 2;
            }
            if ("双语字幕".equals(lan.attr("title"))) {
                sum += 1 << 0;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Long.valueOf("");
        int sum = 0;
        sum += 1 << 0;
        System.out.println(sum);
    }
}
