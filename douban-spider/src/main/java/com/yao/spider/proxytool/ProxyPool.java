package com.yao.spider.proxytool;

import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.proxytool.parses.ip181.Ip181ProxyListParser;
import com.yao.spider.proxytool.parses.ip66.Ip66ProxyListParser;
import com.yao.spider.proxytool.parses.kuaidaili.KuaidailiProxyListParser;
import com.yao.spider.proxytool.parses.mimiip.MimiipProxyListParser;
import com.yao.spider.proxytool.parses.xicidaili.XicidailiProxyListParser;
import com.yao.spider.core.constants.ProxyConstants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by 单耀 on 2018/1/27.
 */
public class ProxyPool {
    //这里也可以简单粗暴的使用sychronized，因为写操作次数远大于读操作，读写锁的意义并不是特别大
    public final static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public final static Set<Proxy> proxySet = new HashSet<Proxy>();

    public static DelayQueue<Proxy> proxyQueue = new DelayQueue<Proxy>();

    public static final Map<String,Class> proxyMap = new HashMap<String, Class>();

    static {
        for (int i = 1; i <= 66; i++) {
            proxyMap.put("https://www.kuaidaili.com/free/intr/"+ i +"/", KuaidailiProxyListParser.class);
            if (!ProxyConstants.anonymousFlag) {
                proxyMap.put("https://www.kuaidaili.com/free/inha/" + i + "/", KuaidailiProxyListParser.class);//高匿
            }
        }
        int pages = 8;
        for (int i = 1; i <= pages; i++) {
            proxyMap.put("http://www.xicidaili.com/wt/" + i + ".html", XicidailiProxyListParser.class);
            proxyMap.put("http://www.xicidaili.com/nn/" + i + ".html", XicidailiProxyListParser.class);
            proxyMap.put("http://www.xicidaili.com/wn/" + i + ".html", XicidailiProxyListParser.class);
            proxyMap.put("http://www.xicidaili.com/nt/" + i + ".html", XicidailiProxyListParser.class);
            proxyMap.put("http://www.ip181.com/daili/" + i + ".html", Ip181ProxyListParser.class);
            proxyMap.put("http://www.mimiip.com/gngao/" + i, MimiipProxyListParser.class);//高匿
            proxyMap.put("http://www.mimiip.com/gnpu/" + i, MimiipProxyListParser.class);//普匿
            proxyMap.put("http://www.66ip.cn/" + i + ".html", Ip66ProxyListParser.class);
            for (int j = 1; j < 34; j++) {
                proxyMap.put("http://www.66ip.cn/areaindex_" + j + "/" + i + ".html", Ip66ProxyListParser.class);
            }
        }
    }
}
