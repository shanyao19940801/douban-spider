package com.yao.spider.proxytool.parses.ip66;

import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.core.constants.ProxyConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 单耀 on 2017/12/17.
 */
public class Ip66ProxyListParser implements IPageParser<Proxy> {
    public List<Proxy> parser(String content) {
        List<Proxy> proxyList = new ArrayList<Proxy>();
        if (content == null || content.equals("")){
            return proxyList;
        }
        Document document = Jsoup.parse(content);
        Elements elements = document.select("table tr:gt(1)");
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            proxyList.add(new Proxy(ip, Integer.valueOf(port), ProxyConstants.TIME_INTERVAL, "ip66"));
        }
        return proxyList;
    }
}
