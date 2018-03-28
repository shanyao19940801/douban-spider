package com.yao.spider.douban.parsers.move;

import com.yao.spider.douban.entity.move.Move;
import com.yao.spider.core.parser.IPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by shanyao on 2018/3/14.
 */
public class MoveDetailInfoParser implements IPageParser<Move> {
    public List<Move> parser(String html) {
        Move move = new Move();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div#info");
        //导演
        Elements directs = elements.select("[rel=v:directedBy]");
        String _direct = move.getDirector();
        for (Element element : directs) {
            if (_direct != null) {
                _direct += "/" + element.text();
            } else {
                _direct = element.text();
            }
        }
        move.setDirector(_direct);
        //编剧
        Elements eAttrs = elements.select("span.attrs");
        Elements screenWriters = eAttrs.get(1).select("a");
        String _screenW = move.getScreenwriter();
        for (Element element : screenWriters) {
            if (_screenW != null) {
                _screenW += "/" + element.text();
            } else {
                _screenW = element.text();
            }
        }

        Elements elementsPL = elements.select("span.pl");
        // TODO 语言
        // TODO 又名

        //片长 // TODO: 2018/2/4
        Elements runtime = elements.select("[property=v:runtime]");
        move.setRuntime(runtime.first().text());
        //IMD连接
        Elements imdb = elements.select("[rel=nofollow]");
        move.setImdb(imdb.first().text());

        return null;
    }
}
