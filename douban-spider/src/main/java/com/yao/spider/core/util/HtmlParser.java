package com.yao.spider.core.util;

import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.parser.IPageParser;

import java.util.List;

public class HtmlParser {
    IPageParser pageParser;

    public List parser(String html,String type) {
        pageParser = ParserFactory.getParserByProductType(type);
        return pageParser.parser(html);
    }
}
