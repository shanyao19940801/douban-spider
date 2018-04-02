package com.yao.spider.proxytool.parses.kuaidaili;

import com.yao.spider.core.constants.ProxyConstants;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.proxytool.entity.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/3/28.
 * 代理地址：https://www.kuaidaili.com/free/inha/2223/
 */
public class KuaidailiProxyListParser  implements IPageParser<Proxy> {
    public List<Proxy> parser(String context) {
        Document document = Jsoup.parse(context);
        Elements tbody = document.select("tbody");
        Elements tr = tbody.select("tr");
        List<Proxy> proxyList = new ArrayList<Proxy>(tr.size());
        for (Element element : tr) {
            String ip = element.select("[data-title=IP]").text();
            String port = element.select("[data-title=PORT]").text();
            if (ip != null && !"".equals(ip) && port != null && !"".equals(port)) {
                Proxy proxy = new Proxy(ip,Integer.valueOf(port), ProxyConstants.TIME_INTERVAL, "kauidaili");
                proxyList.add(proxy);
            }
        }
        return proxyList;
    }
}
