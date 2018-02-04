package com.yao.douban.douban.parsers.move;

import com.yao.douban.douban.entity.move.Move;
import com.yao.douban.douban.parsers.DoubanPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class MoveParser implements DoubanPageParser<Move>{
    public  List<Move> parser(String html) {
        List<Move> list = new ArrayList<Move>();
        System.out.println(html);

        Document document = Jsoup.parse(html);
        Elements elements = document.select("div#info");
        return list;
    }
}
