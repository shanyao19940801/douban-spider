package com.yao.spider.proxytool.parses.xicidaili;

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
 * Created by 单耀 on 2017/12/5.
 */
public class XicidailiProxyListParser implements IPageParser<Proxy> {
    public List<Proxy> parser(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table[id=ip_list] tr[class]");
        List<Proxy> proxyList = new ArrayList<Proxy>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(1)").first().text();
            String port  = element.select("td:eq(2)").first().text();
            proxyList.add(new Proxy(ip, Integer.valueOf(port), ProxyConstants.TIME_INTERVAL, "xicidaili"));
        }
        return proxyList;
    }
}
