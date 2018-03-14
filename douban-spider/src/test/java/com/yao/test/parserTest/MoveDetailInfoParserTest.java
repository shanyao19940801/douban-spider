package com.yao.test.parserTest;

import com.yao.douban.douban.DoubanHttpClient;
import com.yao.douban.douban.parsers.DoubanPageParser;
import com.yao.douban.douban.parsers.DoubanParserFactory;
import com.yao.douban.douban.parsers.move.MoveDetailInfoParser;
import com.yao.douban.douban.parsers.move.MoveParserDeprecated;
import com.yao.douban.proxytool.entity.Page;
import org.junit.Test;

import static org.junit.Assert.*;

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