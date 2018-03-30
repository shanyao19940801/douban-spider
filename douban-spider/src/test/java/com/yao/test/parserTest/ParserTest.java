package com.yao.test.parserTest;

import com.yao.spider.core.entity.Page;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.douban.entity.move.Move;
import com.yao.spider.douban.parsers.move.MoveParser;
import com.yao.spider.douban.parsers.move.MoveParserDeprecated;
import com.yao.douban.douban.parsers.move.TestConsant;

import java.io.IOException;
import java.util.List;

/**
 * Created by 单耀 on 2018/2/2.
 */
public class ParserTest {
    public static void main(String[] args) {
        List<Move> moves = moveListTest();
        String url = moves.get(0).getUrl();
        url += "?tag=%E7%83%AD%E9%97%A8&from=gaia";
        try {
            IPageParser parser = ParserFactory.getParserClass(MoveParserDeprecated.class);
            Page page = DoubanHttpClient.getInstance().getPage(url);
            parser.parser(page.getHtml());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //获取电影列表
    private static List<Move> moveListTest() {
        IPageParser parser = ParserFactory.getParserClass(MoveParser.class);
//        List<MoveList> list = parser.parser("");
        try {
//            Page page = DoubanHttpClient.getInstance().getPage(Constants.STRTY_URL_MOVE);
            List<Move> moves = parser.parser(TestConsant.movelist);
            return moves;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
