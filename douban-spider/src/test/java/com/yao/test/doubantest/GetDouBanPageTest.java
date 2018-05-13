package com.yao.test.doubantest;

import com.yao.spider.core.entity.Page;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.http.client.BaseHttpClient;
import com.yao.spider.core.http.util.HttpClientUtil;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.douban.DoubanHttpClient;
import com.yao.spider.douban.entity.move.Move;
import com.yao.spider.douban.parsers.move.MoveListParser;
import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.proxytool.entity.Proxy;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.util.List;

/**
 * Created by 单耀 on 2018/1/28.
 */
public class GetDouBanPageTest {
    public static void main(String[] args) {
        try {
//            String url = "https://movie.douban.com/j/chart/top_list_count?type=11&interval_id=100:90";
            String url = "https://movie.douban.com/j/chart/top_list?type=11&interval_id=90%3A80&action=&start=0&limit=1";
            Page page = BaseHttpClient.getInstance().getPage(url);
            IPageParser parser = ParserFactory.getParserClass(MoveListParser.class);
            List<Move> moveList = parser.parser(page.getHtml());
            Proxy proxy1 = new Proxy("106.58.123.193",80,1000,"1");
            ProxyPool.proxyQueue.add(proxy1);
            HttpHost proxy = new HttpHost(proxy1.getIp(), proxy1.getPort());
            HttpGet request = new HttpGet(url);
            request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
//            Page page = DoubanHttpClient.getInstance().getPage(request);
//            HttpGet request = new HttpGet(Constants.STRTY_URL_MOVE);
            HttpClientUtil.getResponse(request);
            System.out.println(page.getHtml());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
