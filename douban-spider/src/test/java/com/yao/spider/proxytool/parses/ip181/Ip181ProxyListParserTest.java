package com.yao.spider.proxytool.parses.ip181;

import com.yao.spider.core.entity.Page;
import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.proxytool.ProxyHttpClient;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.proxytool.parses.ip66.Ip66ProxyListParser;
import com.yao.spider.proxytool.parses.mimiip.MimiipProxyListParser;
import com.yao.spider.proxytool.parses.xicidaili.XicidailiProxyListParser;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by shanyao on 2018/4/2.
 */
public class Ip181ProxyListParserTest {
    @Test
    public void parser() throws Exception {
        Page page = ProxyHttpClient.getInstance().getPage("http://www.ip181.com/daili/1.html");
        IPageParser pageParser = ParserFactory.getParserClass(Ip181ProxyListParser.class);
        pageParser.parser(page.getHtml());
    }

    @Test
    public void test1() throws IOException {
        Page page = ProxyHttpClient.getInstance().getPage("http://www.xicidaili.com/nt/1.html");
        IPageParser pageParser = ParserFactory.getParserClass(XicidailiProxyListParser.class);
        List<Proxy> list = pageParser.parser(page.getHtml());
    }
    @Test
    public void test2() throws IOException {
        Page page = ProxyHttpClient.getInstance().getPage("http://www.mimiip.com/gngao/1");
        IPageParser pageParser = ParserFactory.getParserClass(MimiipProxyListParser.class);
        List<Proxy> list = pageParser.parser(page.getHtml());
    }
    @Test
    public void test3() throws IOException {
        Page page = ProxyHttpClient.getInstance().getPage("http://www.66ip.cn/1.html");
        IPageParser pageParser = ParserFactory.getParserClass(Ip66ProxyListParser.class);
        List<Proxy> list = pageParser.parser(page.getHtml());
    }

}