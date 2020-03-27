package com.yao.spider.core.http;


import com.yao.spider.common.exception.ConvertException;
import com.yao.spider.common.exception.HttpClientException;
import com.yao.spider.common.util.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpClientManagerV2 {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientManagerV2.class);

    private PoolingHttpClientConnectionManager cm = null;
    private CloseableHttpClient httpClient;

    public static final String defaultCharsetStr = "UTF-8";
    public static final Charset defaultCharset = Charset.forName(defaultCharsetStr);
    public static final String JsonContentType = "application/json; charset=UTF-8";
    public static final String xmlContentType = "application/xml; charset=UTF-8";
    public static final String formContentType = "multipart/form-data; charset=UTF-8";

    public static final String CONTENT_TYPE = "Content-Type";
    //	private static final Integer BUFFER_SIZE = 4096;

    public static final String ACCEPT = "Accept";

    public static final String ACCEPT_JSON = "application/json";

    public static final String ACCEPT_XML = "application/xml";

    private HttpClientManager httpClientManager;
    private boolean isUseV1 = false;

    private ScheduledExecutorService idleConnectionCloseExecutor = Executors
            .newSingleThreadScheduledExecutor();

    static class MyConnectionSocketFactory extends PlainConnectionSocketFactory {
        
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            if (socksaddr != null) {
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
                return new Socket(proxy);
            } else {
                return super.createSocket(context);
            }

        }

        
        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                    InetSocketAddress localAddress, HttpContext context) throws IOException {
            // Convert address to unresolved
            //			InetSocketAddress unresolvedRemote = InetSocketAddress
            //					.createUnresolved(remoteAddress.getHostName(), remoteAddress.getPort());
            //
            //			logger.info("hostname:{}, remote:{}", host.getHostName(), remoteAddress.getAddress().toString());

            return super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
        }
    }

    static class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {

        public MySSLConnectionSocketFactory(final SSLContext sslContext) {
            // You may need this verifier if target site's certificate is not secure
            super(sslContext, ALLOW_ALL_HOSTNAME_VERIFIER);
        }

        
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            if (socksaddr != null) {
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
                return new Socket(proxy);
            } else {
                return super.createSocket(context);
            }
        }

        
        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                    InetSocketAddress localAddress, HttpContext context) throws IOException {
            // Convert address to unresolved
            //			InetSocketAddress unresolvedRemote = InetSocketAddress
            //					.createUnresolved(remoteAddress.getHostName(), remoteAddress.getPort());
            return super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
        }
    }

    //	static class FakeDnsResolver implements DnsResolver {
    //		
    //		public InetAddress[] resolve(String host) throws UnknownHostException {
    //			// Return some fake DNS record for every request, we won't be using it
    //			return new InetAddress[] { InetAddress.getByAddress(new byte[] { 1, 1, 1, 1 }) };
    //		}
    //	}

    public HttpClientManagerV2(int connectionTimeOut, int soTimeOut, final Set<String> excludeProxyHosts,
            String proxyDomain, Integer proxyPort){
        this(connectionTimeOut, soTimeOut, excludeProxyHosts, proxyDomain, proxyPort, null);
    }

    public HttpClientManagerV2(int connectionTimeOut, int soTimeOut, final Set<String> excludeProxyHosts,
                               String proxyDomain, Integer proxyPort, String provider) {
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionTimeOut)
                .setConnectTimeout(connectionTimeOut)
                .setSocketTimeout(soTimeOut)
                .build();


        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
            
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepalive = super.getKeepAliveDuration(response, context);
                if (keepalive == -1) {
                    keepalive = 5000;
                }
                return keepalive;
            }
        });
        httpClientBuilder.setConnectionReuseStrategy(new DefaultConnectionReuseStrategy() {
            
            public boolean keepAlive(final HttpResponse response,
                                     final HttpContext context) {
                boolean keepAlive = false;
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    keepAlive = super.keepAlive(response, context);
                }
                return keepAlive;
            }
        });

        //socks proxy
        if (!StringUtils.isEmpty(proxyDomain) && proxyPort != null) {
            final InetSocketAddress socksaddr = new InetSocketAddress(proxyDomain, proxyPort);
            DefaultRoutePlanner routePlanner = new DefaultRoutePlanner(null) {
                
                public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException {
                    String hostname = (host != null ? host.getHostName() : null);
                    if (hostname == null) {
                        // bypass proxy for that hostname
                        return super.determineRoute(host, request, context);
                    }
                    if (hostname.toLowerCase().endsWith("idc.cedu.cn")) { //内网域名不走代理
                        // bypass proxy for that hostname
                        return super.determineRoute(host, request, context);
                    }
                    if (hostname.toLowerCase().endsWith("idc.changingedu.com")) { //测试域名不走代理
                        // bypass proxy for that hostname
                        return super.determineRoute(host, request, context);
                    }

                    if (excludeProxyHosts != null && excludeProxyHosts.contains(hostname.toLowerCase())) {
                        //						// bypass proxy for that hostname
                        return super.determineRoute(host, request, context);
                    }

                    context.setAttribute("socks.address", socksaddr);
                    return super.determineRoute(host, request, context);
                }
            };
            httpClientBuilder.setRoutePlanner(routePlanner);
            SSLContext ctx = SSLContexts.createSystemDefault();;
            if (provider != null) {
                try {
                    ctx = SSLContexts.custom()
                            .useProtocol(provider)
                            .loadTrustMaterial(null,
                                    new TrustStrategy() {
                                        
                                        public boolean isTrusted(X509Certificate[] chain,
                                                String authType) {
                                            return true;
                                        }
                                    })
                            .build();
                } catch (Exception e) {
                    ctx = SSLContexts.createSystemDefault();
                }
            }
            Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new MyConnectionSocketFactory())
                    .register("https", new MySSLConnectionSocketFactory(ctx)).build();
            cm = new PoolingHttpClientConnectionManager(reg);
        } else {
            cm = new PoolingHttpClientConnectionManager();
        }
        //http proxy
        //		if( !StringUtils.isEmpty(proxyDomain) && proxyPort != null ) {
        //			HttpHost proxy = new HttpHost(proxyDomain, proxyPort, "http");
        //			DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy) {
        //				
        //				public HttpRoute determineRoute(final HttpHost host, final HttpRequest request, final HttpContext context) throws HttpException {
        //					String hostname = (host != null ? host.getHostName() : null);
        //
        //					if (hostname == null) {
        //						return super.determineRoute(host, request, context);
        //					}
        //
        //					if (hostname.toLowerCase().endsWith("idc.cedu.cn")) { //内网域名不走代理
        //						// bypass proxy for that hostname
        //						return new HttpRoute(host);
        //					}
        //
        //					if (excludeProxyHosts != null && excludeProxyHosts.contains(hostname.toLowerCase())) {
        //						// bypass proxy for that hostname
        //						return new HttpRoute(host);
        //					}
        //					return super.determineRoute(host, request, context);
        //				}
        //			};
        //			httpClientBuilder.setRoutePlanner(routePlanner);
        //		}


        httpClient = httpClientBuilder.setConnectionManager(cm).build();
        java.security.Security.setProperty("networkaddress.cache.ttl", "10");
    }

    public void closeIdleStart() {
        idleConnectionCloseExecutor.scheduleWithFixedDelay(new Runnable() {
            
            public void run() {
                logger.trace("start to close expire and idle connections");
                cm.closeExpiredConnections();
                cm.closeIdleConnections(30, TimeUnit.SECONDS);
            }
        }, 30, 30, TimeUnit.SECONDS);
        logger.info("HttpManager close idle every 30 seconds");
    }

    public void setMaxTotal(int maxTotal) {
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxTotal);
    }

    public void destory() {
        logger.trace("destory");
        idleConnectionCloseExecutor.shutdown();
        cm.shutdown();
    }

    public boolean isV1() {
        if (isUseV1 && httpClientManager != null) {
            return true;
        } else {
            return false;
        }
    }

    
    public InputStream getInputStreamByURI(String url) {
        HttpGet request = new HttpGet(url);

        if (isV1()) {
            return httpClientManager.getInputStreamByURL(url);
        }

        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK && entity != null) {
                return entity.getContent();
            } else {
                try {
                    if (entity != null) {
                        String entityStr = EntityUtils.toString(entity, defaultCharset);
                        logger.warn("getInputStreamByURI error, status:{}, response:{}, url:{}",
                                status, entityStr, url);
                        //						return null;
                        throw new HttpClientException("http get error. url: " + url, status, entityStr);
                    } else {
                        logger.warn("getInputStreamByURI error, status:{}, url:{}", status, url);
                        //						return null;
                        throw new HttpClientException("http get error. url: " + url, status, null);
                    }
                } finally {
                    request.abort();
                }
            }
        } catch (IOException e) {
            request.abort();
            throw new HttpClientException(url + " get error", e);
        }
    }

    
    public Boolean execHeadRequest(String url) {

        HttpHead request = new HttpHead(url);
        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                return true;
            } else if (status == HttpStatus.SC_NOT_FOUND) {
                return false;
            } else {
                logger.warn("execHeadRequest error, status:{}, url:{}", status, url);
                throw new HttpClientException("http head error. url: " + url, status, null);
            }
        } catch (IOException e) {
            request.abort();
            throw new HttpClientException(url + " get error", e);
        } finally {
            request.abort();
        }
    }

    
    public HttpResponse execHeadRequestV2(String url) {
        HttpHead request = new HttpHead(url);
        try {
            HttpResponse response = httpClient.execute(request);
            return response;
        } catch (IOException e) {
            request.abort();
            throw new HttpClientException(url + " get error", e);
        } finally {
            request.abort();
        }
    }

    
    public void execPutFileRequest(String url, File file) {
        HttpPut request = new HttpPut(url);

        try {

            FileEntity fileEntity = new FileEntity(file);

            request.setEntity(fileEntity);
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            if (status == HttpStatus.SC_CREATED || status == HttpStatus.SC_NO_CONTENT) {
                return;
            } else {
                String resp = null;
                if (entity != null) {
                    resp = EntityUtils.toString(entity, defaultCharset);
                    logger.warn("put file error, status:{}, response:{}, url:{}", status, resp, url);
                } else {
                    logger.warn("put file error, status:{}, url:{}", status, url);
                }
                throw new HttpClientException("http post error. url: " + url, status, resp);
            }
        } catch (IOException e) {
            throw new HttpClientException("http put file error. url: " + url, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http put file error. url: " + url, e);
        } finally {
            request.abort();
        }
    }

    
    public String execPutFileRequestWithHeaders(String url, File file, Map<String, String> headers) {
        HttpPut request = new HttpPut(url);

        String resp;
        try {
            FileEntity e = new FileEntity(file);
            request.setEntity(e);
            if(headers != null && !headers.isEmpty()) {
                Iterator response = headers.entrySet().iterator();

                while(response.hasNext()) {
                    Entry status = (Entry)response.next();
                    request.addHeader((String)status.getKey(), (String)status.getValue());
                }
            }

            CloseableHttpResponse response = this.httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if(status != 200) {
                resp = null;
                if(entity != null) {
                    resp = EntityUtils.toString(entity, defaultCharset);
                    logger.warn("put file error, status:{}, response:{}, url:{}", Integer.valueOf(status), resp, url);
                } else {
                    logger.warn("put file error, status:{}, url:{}", Integer.valueOf(status), url);
                }

                throw new HttpClientException("http put file error. url: " + url, Integer.valueOf(status), resp);
            }
            resp = EntityUtils.toString(entity, defaultCharset);
        } catch (IOException var15) {
            throw new HttpClientException("http put file error. url: " + url, var15);
        } catch (HttpClientException var16) {
            throw var16;
        } catch (RuntimeException var17) {
            throw new HttpClientException("http put file error. url: " + url, var17);
        } finally {
            request.abort();
        }
        return resp;
    }

    
    public InputStream execGetInRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers) {


        HttpGet request = null;
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = uriBuilder.build();
            request = new HttpGet(uri);

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
        } catch (URISyntaxException e) {
            throw new HttpClientException("http post error. url: " + url, e);
        }

        try {
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return entity.getContent();
            } else {
                String resp = null;
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    resp = EntityUtils.toString(entity, defaultCharset);
                    logger.warn("get error, status:{}, response:{}, url:{}", status, resp, uri);
                } else {
                    logger.warn("get error, status:{}, url:{}", status, uri);
                }
                throw new HttpClientException("http get error. url: " + uri, status, resp);
            }
        } catch (IOException e) {
            throw new HttpClientException("http get error. url: " + uri, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http get error. url: " + uri, e);
        } finally {
            request.abort();
        }
    }

    public String execGetRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers) {

        if (isV1()) {
            try {
                URI uri = new URI(url);
                return httpClientManager.execGetRequestWithParamsAndHeaders(uri, params, headers);
            } catch (URISyntaxException e) {
                throw new HttpClientException(url + " get error", e);
            }
        }

        HttpGet request = null;
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = uriBuilder.build();
            request = new HttpGet(uri);

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
        } catch (URISyntaxException e) {
            throw new HttpClientException("http post error. url: " + url, e);
        }

        try {
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, defaultCharset);
            } else {
                String resp = null;
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    resp = EntityUtils.toString(entity, defaultCharset);
                    logger.warn("get error, status:{}, response:{}, url:{}", status, resp, uri);
                } else {
                    logger.warn("get error, status:{}, url:{}", status, uri);
                }
                throw new HttpClientException("http get error. url: " + uri, status, resp);
            }
        } catch (IOException e) {
            throw new HttpClientException("http get error. url: " + uri, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http get error. url: " + uri, e);
        } finally {
            request.abort();
        }
    }

    
    public boolean execGetRequest(String url) {

        if (isV1()) {
            return httpClientManager.execGetRequest(url);
        }

        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                return true;
            } else {
                String resp = null;
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    resp = EntityUtils.toString(entity, defaultCharset);
                    logger.warn("get error, status:{}, resonpse:{}, url:{}", status, resp, url);
                } else {
                    logger.warn("get error, status:{}, url:{}", status, url);
                }
                throw new HttpClientException("http get error. url: " + url, status, resp);
            }
        } catch (IOException e) {
            throw new HttpClientException("http get error. url: " + url, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http get error. url: " + url, e);
        } finally {
            request.abort();
        }
    }

    
    public String execGetRequestWithHeader(String url, Map<String, String> header) {
        Map<String, String> param = Collections.emptyMap();
        return execGetRequestWithParamsAndHeaders(url, param, header);
    }

    public InputStream execGetInRequestWithHeader(String url, Map<String, String> header) {
        Map<String, String> param = Collections.emptyMap();
        return execGetInRequestWithParamsAndHeaders(url, param, header);
    }


    
    public String execGetRequestWithParams(String uri, Map<String, String> params) {
        Map<String, String> header = Collections.emptyMap();
        return execGetRequestWithParamsAndHeaders(uri, params, header);
    }

    
    public <T> String execPostFormData(String uri, Map<String, String> formData) {
        Map<String, String> headers = Collections.emptyMap();
        return execPostFormData(uri, formData, headers);
    }

    
    public String execPostFormDataDefaultContentType(String uri, Map<String, String> formData) {
        Map<String, String> headers = Collections.emptyMap();
        return execPostFormDataDefaultContentType(uri, formData, headers);
    }

    
    public String execPostFormDataDefaultContentType(String uri, Map<String, String> formData, Map<String, String> headers) {
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> list = new ArrayList<NameValuePair>(formData.size());
        for (Entry<String, String> entry : formData.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(list, defaultCharsetStr));
            for (Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new HttpClientException("http status code is not 200", response.getStatusLine().getStatusCode(), result);
            } else {
                return result;
            }
        } catch (HttpClientException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpClientException("httpClient error", e);
        }
    }

    
    public String execPostFormData(String uri, Map<String, String> formData, Map<String, String> headers) {

        if (isV1()) {
            return httpClientManager.execPostFormData(uri, formData, headers);
        }

        HttpPost post = new HttpPost(uri);
        List<NameValuePair> list = new ArrayList<NameValuePair>(formData.size());
        for (Entry<String, String> entry : formData.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(list, defaultCharsetStr));
            for (Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }
            post.addHeader(CONTENT_TYPE, formContentType);
            HttpEntity httpEntity = httpClient.execute(post).getEntity();
            return EntityUtils.toString(httpEntity, "UTF-8");
        } catch (Exception e) {
            throw new HttpClientException("", e);
        }
    }


    public String execPostTextRequest(String url, String text) {
        Map<String, String> headers = Collections.emptyMap();
        Map<String, String> params = Collections.emptyMap();
        return execPostTextRequestWithParamsAndHeaders(url, params, headers, text);
    }
    public String execPostTextRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers, String text) {
        StringEntity myEntity = null;
        try {
            myEntity = new StringEntity(text, defaultCharsetStr);
        } catch (UnsupportedCharsetException e) {
            throw new ConvertException("convert entity from json failed. text:" + text);
        }

        return execPostRequestWithParamsAndHeaders(url, params, headers, myEntity);
    }

    public String execInnerPostJsonRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers, String text) {
        StringEntity myEntity = null;
        try {
            myEntity = new StringEntity(text, defaultCharsetStr);
        } catch (UnsupportedCharsetException e) {
            throw new ConvertException("convert entity from json failed. text:" + text);
        }

        return execPostRequestWithParamsAndHeaders(url, params, headers, myEntity);
    }

   /* public <T> String execPostJsonRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers, T obj) {
        return execInnerPostJsonRequestWithParamsAndHeaders(url, params, headers, obj);
    }

    public <T> String execInnerPostJsonRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers, T obj) {
        return execInnerPostJsonRequestWithParamsAndHeaders(url, params, headers, obj);
    }
*/
    private  <T> String execInnerPostJsonRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers, T obj) {
        String json = JsonUtil.getJsonFromObject(obj);
        StringEntity myEntity = null;
        try {
            myEntity = new StringEntity(json, defaultCharsetStr);
        } catch (UnsupportedCharsetException e) {
            throw new ConvertException("convert entity from json failed. json:" + json);
        }

        Map<String, String> header2 = null;
        if (CollectionUtils.isEmpty(headers)) {
            header2 = new HashMap<String, String>(2);
        } else {
            header2 = new HashMap<String, String>(headers);
        }
        header2.put("Content-Type", "application/json");
        return execPostRequestWithParamsAndHeaders(url, params, header2, myEntity);
    }

    public <T> HttpResponseBean execPostJsonRequestWithParamsAndHeadersExpectBean(String url, Map<String, String> params, Map<String, String> headers, T obj) {
        String json = JsonUtil.getJsonFromObject(obj);
        StringEntity myEntity = null;
        try {
            myEntity = new StringEntity(json, defaultCharsetStr);
        } catch (UnsupportedCharsetException e) {
            throw new ConvertException("convert entity from json failed. json:" + json);
        }

        if (CollectionUtils.isEmpty(headers)) {
            headers = new HashMap<String, String>(2);
        }
        headers.put("Content-Type", "application/json");

        HttpPost request = null;
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = uriBuilder.build();
            request = new HttpPost(uri);

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
        } catch (URISyntaxException e) {
            throw new HttpClientException("http post error. url: " + url, e);
        }

        try {
            long startTimeMills = System.currentTimeMillis();
            request.setEntity(myEntity);
            HttpResponse response = httpClient.execute(request);
            long endTimeMills = System.currentTimeMillis();

            int status = response.getStatusLine().getStatusCode();
            logger.trace("execPostRequest, using:{}ms, url:{}, header:{}, params:{}, body:{}, status:{}",
                    (endTimeMills - startTimeMills), url, headers, params, EntityUtils.toString(myEntity), status);

            HttpResponseBean bean = new HttpResponseBean();
            bean.setResponse(EntityUtils.toString(response.getEntity(), defaultCharset));
            bean.setStatus(status);
            return bean;
        } catch (IOException e) {
            throw new HttpClientException("http post error. url: " + uri, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http post error. url: " + uri, e);
        } finally {
            request.abort();
        }
    }

    public HttpResponseBean execPostRequestWithParamsAndHeadersExpectBean(String url, Map<String, String> params, Map<String, String> headers, StringEntity myEntity){
        return execPostRequestWithParamsAndHeadersExpectBean(url, params, headers, myEntity, false);
    }


    public HttpResponseBean execPostRequestWithParamsAndHeadersExpectBean(String url, Map<String, String> params, Map<String, String> headers, String text, boolean isInner){
        StringEntity myEntity = null;
        try {
            myEntity = new StringEntity(text, defaultCharsetStr);
        } catch (UnsupportedCharsetException e) {
            throw new ConvertException("convert entity from json failed. text:" + text);
        }
        return execPostRequestWithParamsAndHeadersExpectBean(url, params, headers, myEntity, isInner);
    }

    private HttpResponseBean execPostRequestWithParamsAndHeadersExpectBean(String url, Map<String, String> params, Map<String, String> headers, StringEntity myEntity, boolean isInner) {
        HttpPost request = null;
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = uriBuilder.build();
            request = new HttpPost(uri);

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
        } catch (URISyntaxException e) {
            throw new HttpClientException("http post error. url: " + url, e);
        }

        try {
            long startTimeMills = System.currentTimeMillis();
            request.setEntity(myEntity);
            HttpResponse response = httpClient.execute(request);
            long endTimeMills = System.currentTimeMillis();

            int status = response.getStatusLine().getStatusCode();
            logger.trace("execPostRequest, using:{}ms, url:{}, header:{}, params:{}, body:{}, status:{}",
                    (endTimeMills - startTimeMills), url, headers, params, myEntity == null ? null :EntityUtils.toString(myEntity), status);

            HttpResponseBean bean = new HttpResponseBean();
            bean.setResponse(EntityUtils.toString(response.getEntity(), defaultCharset));
            bean.setStatus(status);
            return bean;
        } catch (IOException e) {
            throw new HttpClientException("http post error. url: " + uri, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http post error. url: " + uri, e);
        } finally {
            request.abort();
        }
    }

    public <T> HttpResponse execRawPostJsonRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers, T obj) {

        String json = JsonUtil.getJsonFromObject(obj);
        StringEntity myEntity = null;
        try {
            myEntity = new StringEntity(json, defaultCharsetStr);
        } catch (UnsupportedCharsetException e) {
            throw new ConvertException("convert entity from json failed. json:" + json);
        }

        if (CollectionUtils.isEmpty(headers)) {
            headers = new HashMap<String, String>(2);
        }
        headers.put("Content-Type", "application/json");

        HttpPost request = null;
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = uriBuilder.build();
            request = new HttpPost(uri);

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
        } catch (URISyntaxException e) {
            throw new HttpClientException("http post error. url: " + url, e);
        }

        try {
            long startTimeMills = System.currentTimeMillis();
            request.setEntity(myEntity);
            HttpResponse response = httpClient.execute(request);
            long endTimeMills = System.currentTimeMillis();
            int status = response.getStatusLine().getStatusCode();
            logger.trace("execPostRequest, using:{}ms, url:{}, header:{}, params:{}, body:{}, status:{}",
                    (endTimeMills - startTimeMills), url, headers, params, EntityUtils.toString(myEntity), status);
            return response;
        } catch (IOException e) {
            throw new HttpClientException("http post error. url: " + uri, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http post error. url: " + uri, e);
        } finally {
            request.abort();
        }
    }


    public String execPostRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers, StringEntity myEntity) {
        return execPostRequestWithParamsAndHeaders(url, params, headers, myEntity);
    }

    
    public String execDeleteRequest(String url) {
        return execDeleteRequest(url);
    }

    
    public HttpResponse execDeleteRequestReturnResp(String url) {
        HttpDelete request = null;
        try {
            request = new HttpDelete(url);
            return httpClient.execute(request);
        } catch (IOException e) {
            throw new HttpClientException("http delete error. url: " + url, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http delete error. url: " + url, e);
        } finally {
            if (request != null) {
                request.abort();
            }
        }
    }

    public <T> String execPutJsonRequestWithParamsAndHeaders(String uri, Map<String, String> params, Map<String, String> headers, T obj) {

        String json = JsonUtil.getJsonFromObject(obj);
        StringEntity myEntity = null;
        try {
            myEntity = new StringEntity(json, defaultCharsetStr);
        } catch (UnsupportedCharsetException e) {
            throw new ConvertException("convert entity from json failed. json:" + json);
        }

        return execPutRequestWithParamsAndHeaders(uri, params, headers, myEntity);
    }

    public String execPutRequestWithParamsAndHeaders(String url, Map<String, String> params, Map<String, String> headers, StringEntity myEntity) {

        if (isV1()) {
            return httpClientManager.execPutRequestWithParamsAndHeaders(url, params, headers, myEntity);
        }

        HttpPut request = null;
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = uriBuilder.build();
            request = new HttpPut(uri);

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
        } catch (URISyntaxException e) {
            throw new HttpClientException("http post error. url: " + url, e);
        }

        try {
            request.setEntity(myEntity);
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();

            if (status == HttpStatus.SC_OK) {
                return EntityUtils.toString(entity, defaultCharset);
            } else {
                String resp = null;
                if (entity != null) {
                    resp = EntityUtils.toString(entity, defaultCharset);
                    logger.warn("post error, status:{}, response:{}, url:{}", status, resp, uri);
                } else {
                    logger.warn("post error, status:{}, url:{}", status, uri);
                }
                throw new HttpClientException("http post error. url: " + uri, status, resp);
            }
        } catch (IOException e) {
            throw new HttpClientException("http post error. url: " + uri, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http post error. url: " + uri, e);
        } finally {
            request.abort();
        }
    }

    public String execGetRequestWithContent(String url) {
        try {
            URI uri = new URI(url);
            return execGetRequestWithContent(uri);
        } catch (URISyntaxException e) {
            throw new HttpClientException(url + " get error", e);
        }
    }

    public String execGetRequestWithContent(String url, String cookie) {
        try {
            URI uri = new URI(url);
            return execGetRequestWithContent(uri);
        } catch (URISyntaxException e) {
            throw new HttpClientException(url + " get error", e);
        }
    }

    public String execGetRequestWithContent(URI uri) {
        return execGetRequestWithContent(uri, null);
    }

    public String execGetRequestWithContent(URI uri, String cookie) {
        HttpGet request = new HttpGet(uri);
        if (!StringUtils.isEmpty(cookie)) {
            request.addHeader(new BasicHeader("Cookie", cookie));
        }

        try {
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return EntityUtils.toString(entity, defaultCharset);
            } else {
                HttpEntity entity = response.getEntity();
                String resp = null;
                if (entity != null) {
                    resp = EntityUtils.toString(entity, defaultCharset);
                    logger.error("get error, status:{}, resonpse:{}, url:{}", status, resp, uri);
                } else {
                    logger.error("get error, status:{}, url:{}", status, uri);
                }
                throw new HttpClientException("http get error. url: " + uri, status, resp);
            }
        } catch (IOException e) {
            throw new HttpClientException("http get error. url: " + uri, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http get error. url: " + uri, e);
        } finally {
            request.abort();
        }
    }


    public HttpResponse execGetRequestReturnResp(String url) {
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                return response;
            } else {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    logger.warn("get error, status:{}, resonpse:{}, url:{}", new Object[]{status, EntityUtils.toString(entity, defaultCharset), url});
                } else {
                    logger.warn("get error, status:{}, url:{}", status, url);
                }
                throw new HttpClientException("http get error. url: " + url);
            }
        } catch (Exception e) {
            throw new HttpClientException("http get error. url: " + url, e);
        } finally {
            request.abort();
        }
    }

    
    public HttpResponseBean execGetRequestWithParmsReturnResp(String url, Map<String, String> params, Map<String, String> headers) {
        HttpGet request = null;
        URI uri = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            uri = uriBuilder.build();
            request = new HttpGet(uri);

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }
        } catch (URISyntaxException e) {
            throw new HttpClientException("http post error. url: " + url, e);
        }

        try {
            HttpResponse response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            HttpResponseBean bean = new HttpResponseBean();
            bean.setResponse(EntityUtils.toString(response.getEntity(), defaultCharset));
            bean.setStatus(status);
            return bean;
        } catch (IOException e) {
            throw new HttpClientException("http get error. url: " + uri, e);
        } catch (HttpClientException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new HttpClientException("http get error. url: " + uri, e);
        } finally {
            request.abort();
        }
    }

    public void setHttpClientManager(HttpClientManager httpClientManager) {
        this.httpClientManager = httpClientManager;
    }

    public void setIsUseV1(Boolean isUseV1) {
        this.isUseV1 = isUseV1;
    }

    private static final String ALLOWABLE_IP_REGEX = "(127[.]0[.]0[.]1)|" + "(localhost)|" +
            "(10[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3})|" +
            "(172[.]((1[6-9])|(2\\d)|(3[01]))[.]\\d{1,3}[.]\\d{1,3})|" +
            "(192[.]168[.]\\d{1,3}[.]\\d{1,3})";

    public static void main(String[] args) throws URISyntaxException {


    }

}
