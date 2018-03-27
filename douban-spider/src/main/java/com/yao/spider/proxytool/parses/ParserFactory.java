package com.yao.spider.proxytool.parses;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 单耀 on 2018/1/27.
 */
public class ParserFactory {

    public static ProxyListPageParser getProxyListPageParser(Class clzz) {
        Map<String,ProxyListPageParser> map = new HashMap<String, ProxyListPageParser>();
        try {
            String parserName = clzz.getSimpleName();
            if (map.containsKey(parserName)) {
                return map.get(parserName);
            }else {
                ProxyListPageParser proxyListPageParser = (ProxyListPageParser) clzz.newInstance();
                map.put(parserName, proxyListPageParser);
                return proxyListPageParser;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
