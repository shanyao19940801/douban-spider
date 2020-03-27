package com.yao.spider.core.http;


import com.google.protobuf.GeneratedMessage.Builder;
import com.yao.spider.common.exception.HttpClientException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParamBean;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HttpClientManager {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientManager.class);

	private PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
	private DefaultHttpClient httpClient;

	public static final String defaultCharsetStr = "UTF-8";
	public static final Charset defaultCharset = Charset.forName(defaultCharsetStr);
	public static final String jsonContentType = "application/json; charset=UTF-8";
	public static final String xmlContentType = "application/xml; charset=UTF-8";
	public static final String formContentType = "multipart/form-data; charset=UTF-8";

	public static final String CONTENT_TYPE = "Content-Type";
	private static final Integer BUFFER_SIZE = 4096;
	
	private ScheduledExecutorService idleConnectionCloseExecutor = Executors
			.newSingleThreadScheduledExecutor();

	public HttpClientManager(int connectionTimeOut, int soTimeOut) {
		HttpParams params = new SyncBasicHttpParams();
		DefaultHttpClient.setDefaultHttpParams(params);
		HttpConnectionParamBean paramsBean = new HttpConnectionParamBean(params);
		paramsBean.setConnectionTimeout(connectionTimeOut);
		paramsBean.setSoTimeout(soTimeOut);

		httpClient = new DefaultHttpClient(cm, params);
		httpClient.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
			@Override
			public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
		        long keepalive = super.getKeepAliveDuration(response, context);		        
		        if( keepalive == -1 ) {
		        	keepalive = 5000;
		        }
		        return keepalive;
		    }
		});
		httpClient.setReuseStrategy(new DefaultConnectionReuseStrategy(){		
			@Override
			public boolean keepAlive(final HttpResponse response,
                    final HttpContext context) {
				boolean keekAlive = false;
				if( HttpStatus.SC_OK == response.getStatusLine().getStatusCode() ) {
					keekAlive = super.keepAlive(response, context);
				}
				return keekAlive;
			}			
		});
		
		java.security.Security.setProperty("networkaddress.cache.ttl", "10");
	}






	public String execPutRequestWithParamsAndHeaders(String uri, Map<String, String> params, Map<String, String> headers, StringEntity myEntity) {
		HttpPut request = new HttpPut(uri);

		if (params != null && !params.isEmpty()) {
			for (Entry<String, String> entry : params.entrySet()) {
				request.getParams().setParameter(entry.getKey(), entry.getValue());
			}
		}

		if(headers != null && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
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
				if( entity != null ) {
					resp = EntityUtils.toString(entity, defaultCharset);
					logger.error("post error, status:{}, response:{}, url:{}", status, resp, uri);
				} else {
					logger.error("post error, status:{}, url:{}", status, uri);
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

	public String execPostFormData(String uri, Map<String, String> fromData, Map<String, String> headers){
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> list = new ArrayList<NameValuePair>(fromData.size());
		for(Entry<String, String> entry : fromData.entrySet()){
			list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(list,defaultCharsetStr));
			for(Entry<String, String> entry : headers.entrySet()){
				post.addHeader(entry.getKey(), entry.getValue());
			}
			HttpEntity httpEntity = httpClient.execute(post).getEntity();
			return EntityUtils.toString(httpEntity, "UTF-8");
		} catch (Exception e) {
			throw new HttpClientException("", e);
		}
	}

	public boolean execGetRequest(String url) {
		try {
			URI uri = new URI(url);
			return execGetRequest(uri);
		} catch (URISyntaxException e) {
			throw new HttpClientException(url + " get error", e);
		}
	}


	public String execGetRequestWithParamsAndHeaders(URI uri, Map<String, String> params, Map<String, String> heads) {
		HttpGet request = new HttpGet(uri);
		if (null != params) {
			for (Entry<String, String> entry : params.entrySet()) {
				request.getParams().setParameter(entry.getKey(), entry.getValue());
			}
		}

		if( heads != null ) {
			for (Entry<String, String> entry : heads.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
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

	public InputStream execGetRequesIntWithParamsAndHeaders(URI uri, Map<String, String> params, Map<String, String> heads) {
		HttpGet request = new HttpGet(uri);
		if (null != params) {
			for (Entry<String, String> entry : params.entrySet()) {
				request.getParams().setParameter(entry.getKey(), entry.getValue());
			}
		}

		if( heads != null ) {
			for (Entry<String, String> entry : heads.entrySet()) {
				request.addHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			int status = response.getStatusLine().getStatusCode();
			if ( status == HttpStatus.SC_OK && entity != null) {
				return entity.getContent();
			} else {
				try {
					if (entity != null) {
						logger.error("getInputStreamByURI error, status:{}, resonpse:{}, url:{}", new Object[] { status, EntityUtils.toString(entity, defaultCharset), uri });
						return null;
					} else {
						logger.error("getInputStreamByURI error, status:{}, url:{}", new Object[] {response.getStatusLine().getStatusCode(), uri });
						return null;
					}
				} finally {
					request.abort();
				}
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

	public InputStream getInputStreamByURL(String url) {
		try {
			logger.debug("getInputStreamByURL, url:{}", url);
			URI uri = new URI(url);
			return getInputStreamByURI(uri);
		} catch (URISyntaxException e) {
			throw new HttpClientException(url + " get error", e);
		}
	}
	public boolean execGetRequest(URI uri) {
		HttpGet request = new HttpGet(uri);
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
					logger.error("get error, status:{}, resonpse:{}, url:{}", status, resp, uri );
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
	public InputStream getInputStreamByURI(URI uri) {
		HttpGet request = new HttpGet(uri);
		try {
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();

			int status = response.getStatusLine().getStatusCode();

			if ( status == HttpStatus.SC_OK && entity != null) {
				return entity.getContent();
			} else {
				try {
					if (entity != null) {
						logger.error("getInputStreamByURI error, status:{}, resonpse:{}, url:{}", new Object[] { status, EntityUtils.toString(entity, defaultCharset), uri });
						return null;
					} else {
						logger.error("getInputStreamByURI error, status:{}, url:{}", new Object[] {response.getStatusLine().getStatusCode(), uri });
						return null;
					}
				} finally {
					request.abort();
				}
			}
		} catch (IOException e) {
			request.abort();
			throw new HttpClientException(uri.toString() + " get error");
		}
	}


}
