package com.yao.test.parserTest;

import com.yao.spider.core.entity.Page;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.douban.parsers.move.MoveDetailInfoParser;
import org.junit.Test;

/**
 * Created by shanyao on 2018/3/14.
 */
public class MoveDetailInfoParserTest {
    @Test
    public void parser() throws Exception {
        String url = "https://movie.douban.com/subject/26346327/";
        IPageParser parser = ParserFactory.getParserClass(MoveDetailInfoParser.class);
        Page page = DoubanHttpClient.getInstance().getPage(url);
        parser.parser(page.getHtml());
    }

}