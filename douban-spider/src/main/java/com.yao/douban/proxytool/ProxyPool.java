package com.yao.douban.proxytool;

import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.ip66.Ip66ProxyListPageParser;
import com.yao.douban.proxytool.parses.ip181.Ip181ProxyListPageParser;
import com.yao.douban.proxytool.parses.mimiip.MimiipProxyListPageParser;
import com.yao.douban.proxytool.parses.xicidaili.XicidailiProxyListPageParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * Created by 单耀 on 2018/1/27.
 */
public class ProxyPool {

    public static final DelayQueue<Proxy> proxyQueue = new DelayQueue<Proxy>();

    public static final Map<String,Class> proxyMap = new HashMap<String, Class>();

    static {
        int pages = 8;
        for (int i = 1; i <= pages; i++) {
            proxyMap.put("http://www.xicidaili.com/wt/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/nn/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/wn/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/nt/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.ip181.com/daili/" + i + ".html", Ip181ProxyListPageParser.class);
            proxyMap.put("http://www.mimiip.com/gngao/" + i, MimiipProxyListPageParser.class);//高匿
            proxyMap.put("http://www.mimiip.com/gnpu/" + i, MimiipProxyListPageParser.class);//普匿
            proxyMap.put("http://www.66ip.cn/" + i + ".html", Ip66ProxyListPageParser.class);
            for (int j = 1; j < 34; j++) {
                proxyMap.put("http://www.66ip.cn/areaindex_" + j + "/" + i + ".html", Ip66ProxyListPageParser.class);
            }
        }
    }
}
