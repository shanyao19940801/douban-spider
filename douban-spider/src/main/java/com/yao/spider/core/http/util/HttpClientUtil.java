package com.yao.spider.core.http.util;

import com.yao.spider.core.constants.ProxyConstants;
import org.apache.http.Consts;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

/**
 * Created by 单耀 on 2018/1/25.
 *
 */
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static HttpHost proxy;
    private static RequestConfig requestConfig;
    private final static String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36";
    private static CloseableHttpClient httpClient;
    private static CookieStore cookieStore = new BasicCookieStore();
    static {
        initHttpClient();
    }

    private static void initHttpClient()  {
        try {
            //采用绕过验证的方式处理https请求
            SSLContext sslContext= SSLContexts.custom()
                    .loadTrustMaterial(KeyStore.getInstance(KeyStore.getDefaultType()), new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] x509Certificates, String s)
                                throws CertificateException {
                            return true;
                        }
                    }).build();
            // 设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().
                    register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https",new SSLConnectionSocketFactory(sslContext))
                    .build();
            //配备连接池管理器
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            //
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(ProxyConstants.SocketTimeout).setTcpNoDelay(true).build();
            connectionManager.setDefaultSocketConfig(socketConfig);
            ConnectionConfig connectionConfig =
                    ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                            .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();
            connectionManager.setDefaultConnectionConfig(connectionConfig);
            connectionManager.setMaxTotal(500);
            connectionManager.setDefaultMaxPerRoute(300);
            HttpRequestRetryHandler retryHandler = createHttpRequestRetryHandler();

            HttpClientBuilder httpClientBuilder =
                    HttpClients.custom().setConnectionManager(connectionManager)
                            .setDefaultCookieStore(new BasicCookieStore())
                            .setRetryHandler(retryHandler)
                            .setUserAgent(userAgent);

            if (proxy != null) {
                httpClientBuilder.setRoutePlanner(new DefaultProxyRoutePlanner(proxy)).build();
            }

            httpClient = httpClientBuilder.build();

            requestConfig = RequestConfig.custom().setConnectionRequestTimeout(ProxyConstants.ConnectionTimeout)
                    .setSocketTimeout(ProxyConstants.SocketTimeout)
                    .setConnectTimeout(ProxyConstants.TIMEOUT)
                    .setCookieSpec(ProxyConstants.STANDARD)
                    .build();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        try {
            return SSLContexts.custom()
                    .loadTrustMaterial(KeyStore.getInstance(KeyStore.getDefaultType()), new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] x509Certificates, String s)
                                throws CertificateException {
                            return true;
                        }
                    }).build();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return null;

      /*  SSLContext sslContext = SSLContext.getInstance("SSLv3");
        X509TrustManager trustManager = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        sslContext.init(null, new TrustManager[] { trustManager}, null);
        return sslContext;*/
    }

    //自定义重试次数以及重试业务处理
    //参考https://www.jianshu.com/p/481cbfcd3f13
    public static HttpRequestRetryHandler createHttpRequestRetryHandler() {
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException e, int retrytimes, HttpContext httpContext) {
                //返回false表示不再重试，返回true表示继续重试
                if (retrytimes > 2) {
                    return false;
                }

                if (e instanceof UnknownHostException || e instanceof ConnectTimeoutException || e instanceof InterruptedIOException) {
                    return true;
                }

                HttpRequest request = HttpClientContext.adapt(httpContext).getRequest();
                // 如果请求被认为是幂等的，那么就重试。即重复执行不影响程序其他效果的
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }

                return false;
            }
        };
        return httpRequestRetryHandler;
    }

    public static CloseableHttpResponse getResponse(HttpRequestBase request) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        if (request.getConfig() == null) {
            request.setConfig(requestConfig);
        }
        request.setHeader("User-Agent", ProxyConstants.userAgentArray[new Random().nextInt(ProxyConstants.userAgentArray.length)]);
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        return httpClient.execute(request, context);
    }

    public static CloseableHttpResponse getResponse(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        return getResponse(request);
    }

    public static RequestConfig.Builder getRequestConfigBuilder() {
        return RequestConfig.custom().setConnectionRequestTimeout(ProxyConstants.ConnectionTimeout)
                .setSocketTimeout(ProxyConstants.SocketTimeout)
                .setConnectTimeout(ProxyConstants.TIMEOUT)
                .setCookieSpec(ProxyConstants.STANDARD);
    }

}
