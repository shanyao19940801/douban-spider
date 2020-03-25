package com.yao.spider.core.factory;

import com.yao.spider.core.constants.ParserConstants;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.douban.parsers.move.MoveDetailInfoParser;
import com.yao.spider.douban.parsers.move.MoveParser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class ParserFactory {
    private static Map<String,Class> parseMap = new HashMap<String, Class>();
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
    public static IPageParser getParserByProductType(String type) {
        try {
            //利用java放射机制
            return (IPageParser) parseMap.get(type).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }

    static {
        parseMap.put(ParserConstants.MOVE_LIST,MoveParser.class);
        parseMap.put(ParserConstants.MOVE_DETAIL,MoveDetailInfoParser.class);

    }


}
