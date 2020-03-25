package com.yao.spider.zimuku.parsers;

import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.zimuku.domain.ZimuInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/3/28.
 */
public class ZimuParser implements IPageParser<ZimuInfo>{
    public List<ZimuInfo> parser(String html) {
        Document document = Jsoup.parse(html);
        Elements oddList =document.select(".odd");
        Elements evenList =document.select(".even");
        System.out.println(11);
        /*String _direct = move.getDirector();
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

        Elements runtime = elements.select("[property=v:runtime]");
        move.setRuntime(runtime.first().text());
        //IMD连接
        Elements imdb = elements.select("[rel=nofollow]");
        move.setImdb(imdb.first().text());*/
        return null;
    }
}
