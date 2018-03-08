package com.yao.test.parserTest;

import com.yao.douban.douban.DoubanHttpClient;
import com.yao.douban.douban.entity.move.Move;
import com.yao.douban.douban.parsers.DoubanPageParser;
import com.yao.douban.douban.parsers.DoubanParserFactory;
import com.yao.douban.douban.parsers.move.MoveParser;
import com.yao.douban.douban.parsers.move.MoveParserDeprecated;
import com.yao.douban.douban.parsers.move.TestConsant;
import com.yao.douban.proxytool.entity.Page;

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
            DoubanPageParser parser = DoubanParserFactory.getDoubanParserFactory(MoveParserDeprecated.class);
            Page page = DoubanHttpClient.getInstance().getPage(url);
            parser.parser(page.getHtml());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //获取电影列表
    private static List<Move> moveListTest() {
        DoubanPageParser parser = DoubanParserFactory.getDoubanParserFactory(MoveParser.class);
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
