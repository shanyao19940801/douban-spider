package com.yao.spider.proxytool.http.client;

import com.yao.spider.proxytool.entity.Page;
import com.yao.spider.proxytool.http.util.HttpClientUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by 单耀 on 2018/1/27.
 */
public abstract class AbstractHttpClient {

    public Page getPage(String url) throws IOException {
        return getPage(url, "UTF-8");
    }
    public Page getPage(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = null;
        response = HttpClientUtil.getResponse(request);
        Page page = new Page();
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setUrl(request.getURI().toString());
        page.setHtml(EntityUtils.toString(response.getEntity()));
        return page;
    }

    public Page getPage(String url, String charset) throws IOException {
        Page page = new Page();
        CloseableHttpResponse response = null;
        response = HttpClientUtil.getResponse(url);
        page.setUrl(url);
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setHtml(EntityUtils.toString(response.getEntity(), charset));
        return page;
    }

}

