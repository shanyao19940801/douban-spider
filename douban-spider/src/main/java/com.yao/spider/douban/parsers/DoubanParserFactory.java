package com.yao.spider.douban.parsers;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class DoubanParserFactory {
    public static DoubanPageParser getDoubanParserFactory(Class clzz) {
        try {
            return (DoubanPageParser) clzz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }
}
