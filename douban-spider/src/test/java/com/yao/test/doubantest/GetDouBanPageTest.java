package com.yao.test.doubantest;

import com.yao.douban.core.util.Constants;
import com.yao.douban.douban.DoubanHttpClient;
import com.yao.douban.proxytool.ProxyPool;
import com.yao.douban.proxytool.entity.Page;
import com.yao.douban.proxytool.ProxyHttpClient;
import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.http.util.HttpClientUtil;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

/**
 * Created by 单耀 on 2018/1/28.
 */
public class GetDouBanPageTest {
    public static void main(String[] args) {
        try {
//            String url = "https://movie.douban.com/j/chart/top_list_count?type=11&interval_id=100:90";
            String url = "https://movie.douban.com/j/chart/top_list_count?type=12&interval_id=100:90";
            Proxy proxy1 = new Proxy("106.58.123.193",80,1000,"1");
            ProxyPool.proxyQueue.add(proxy1);
            HttpHost proxy = new HttpHost(proxy1.getIp(), proxy1.getPort());
            HttpGet request = new HttpGet(url);
            request.setConfig(HttpClientUtil.getRequestConfigBuilder().setProxy(proxy).build());
            Page page = DoubanHttpClient.getInstance().getPage(request);
//            HttpGet request = new HttpGet(Constants.STRTY_URL_MOVE);
            HttpClientUtil.getResponse(request);
            System.out.println(page.getHtml());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
