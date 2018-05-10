package com.yao.spider.core.factory;

import com.yao.spider.core.parser.IPageParser;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class ParserFactory {
    public static IPageParser getParserClass(Class clzz) {
        try {
            //利用java放射机制
            return (IPageParser) clzz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }
}
