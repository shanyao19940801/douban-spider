package com.yao.test.parserTest;

import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.douban.parsers.DoubanPageParser;
import com.yao.spider.douban.parsers.DoubanParserFactory;
import com.yao.spider.douban.parsers.move.MoveDetailInfoParser;
import com.yao.spider.proxytool.entity.Page;
import org.junit.Test;

/**
 * Created by shanyao on 2018/3/14.
 */
public class MoveDetailInfoParserTest {
    @Test
    public void parser() throws Exception {
        String url = "https://movie.douban.com/subject/26346327/";
        DoubanPageParser parser = DoubanParserFactory.getDoubanParserFactory(MoveDetailInfoParser.class);
        Page page = DoubanHttpClient.getInstance().getPage(url);
        parser.parser(page.getHtml());
    }

}