package com.yao.test.parserTest;

import com.yao.douban.douban.entity.move.Move;
import com.yao.douban.douban.parsers.DoubanPageParser;
import com.yao.douban.douban.parsers.DoubanParserFactory;
import com.yao.douban.douban.parsers.move.MoveParser;

import java.util.List;

/**
 * Created by 单耀 on 2018/2/2.
 */
public class ParserTest {
    public static void main(String[] args) {
        DoubanPageParser parser = DoubanParserFactory.getDoubanParserFactory(MoveParser.class);
        List<Move> list = parser.parser("");
        System.out.println(list.get(0).getId());


    }
}
