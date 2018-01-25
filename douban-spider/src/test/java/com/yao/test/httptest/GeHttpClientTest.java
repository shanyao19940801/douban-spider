package com.yao.test.httptest;

import com.yao.douban.http.util.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by 单耀 on 2018/1/25.
 */
public class GeHttpClientTest {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("https://movie.douban.com/j/search_subjects?type=movie&tag=%E8%B1%86%E7%93%A3%E9%AB%98%E5%88%86&sort=recommend&page_limit=20&page_start=20");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");

        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity,"utf-8"));
    }

    public void test1() throws IOException {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
                // Create a local instance of cookie store
                CookieStore cookieStore = new BasicCookieStore();

                // Create local HTTP context
                HttpClientContext localContext = HttpClientContext.create();
                // Bind custom cookie store to the local context
                localContext.setCookieStore(cookieStore);

                HttpGet httpget = new HttpGet("http://localhost/");
                System.out.println("Executing request " + httpget.getRequestLine());

                // Pass local context as a parameter
                CloseableHttpResponse response = httpclient.execute(httpget, localContext);
                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    List<Cookie> cookies = cookieStore.getCookies();
                    for (int i = 0; i < cookies.size(); i++) {
                        System.out.println("Local cookie: " + cookies.get(i));
                    }
                    EntityUtils.consume(response.getEntity());
                } finally {
                    response.close();
                }
            } finally {
                httpclient.close();
            }

    }
}
