package com.yao.test.doubantest;

import com.yao.douban.core.util.Constants;
import com.yao.douban.proxytool.entity.Page;
import com.yao.douban.proxytool.ProxyHttpClient;
import com.yao.douban.proxytool.http.util.HttpClientUtil;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

/**
 * Created by 单耀 on 2018/1/28.
 */
public class GetDouBanPageTest {
    public static void main(String[] args) {
        try {
            Page page = ProxyHttpClient.getInstance().getPage(Constants.STRTY_URL);
            HttpGet request = new HttpGet(Constants.STRTY_URL);
            HttpClientUtil.getResponse(request);
            System.out.println(page.getHtml());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
