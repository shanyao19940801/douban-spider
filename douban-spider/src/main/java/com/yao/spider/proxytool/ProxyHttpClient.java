package com.yao.spider.proxytool;

import com.yao.spider.core.http.client.BaseHttpClient;
import com.yao.spider.proxytool.task.ProxyPageTask;
import com.yao.spider.proxytool.task.ProxySerializeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by 单耀 on 2018/1/27.
 */
public class ProxyHttpClient extends BaseHttpClient {
    private static Logger logger = LoggerFactory.getLogger(ProxyHttpClient.class);
    public static volatile boolean isContinue = true;
    private volatile static ProxyHttpClient instance;
    //下载代理页面的线程池
    private ThreadPoolExecutor proxyDoloadThreadExector;

    private ThreadPoolExecutor proxyProxyTestExector;
    public static ProxyHttpClient getInstance() {
        if (instance == null) {
            synchronized (ProxyHttpClient.class) {
                if (instance == null) {
                    instance = new ProxyHttpClient();
                }
            }
        }
        return instance;
    }

    public ProxyHttpClient() {
        initThreadPool();
    }

    //初始化线程池
    private void initThreadPool() {
        //线程通过线程工厂创建，这样每个线程都会有名字，以便于
        proxyDoloadThreadExector = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "proxyDoloadThreadExector" + r.hashCode());
                    }
                });

        proxyProxyTestExector = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "proxyProxyTestExector" + r.hashCode());
                    }
                });
    }

    public ThreadPoolExecutor getProxyDoloadThreadExector() {
        return proxyDoloadThreadExector;
    }

    public void setProxyDoloadThreadExector(ThreadPoolExecutor proxyDoloadThreadExector) {
        this.proxyDoloadThreadExector = proxyDoloadThreadExector;
    }

    public ThreadPoolExecutor getProxyProxyTestExector() {
        return proxyProxyTestExector;
    }

    public void setProxyProxyTestExector(ThreadPoolExecutor proxyProxyTestExector) {
        this.proxyProxyTestExector = proxyProxyTestExector;
    }

    public void startProxy() {
        //是否爬取新的代理
        new Thread(new Runnable() {
            public void run() {
//                while(isContinue) {
                for (String url : ProxyPool.proxyMap.keySet()) {
                    proxyDoloadThreadExector.execute(new ProxyPageTask(url, false));
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                try {
                    Thread.sleep(1000 * 60 * 60);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
//                }
            }
        }).start();

        //序列化代理线程
        logger.info("启动序列化代理线程");
        new Thread(new ProxySerializeTask()).start();

    }

    /**
     * 停止爬取代理，isNow:true采用shutdonwNow，isNow:false 采用shutdown
     * @param isNow
     */
    public void shutStopSpiderProxy(boolean isNow) {
        if (isNow) {
            this.proxyDoloadThreadExector.shutdownNow();
            this.proxyProxyTestExector.shutdownNow();
        } else {
            this.proxyDoloadThreadExector.shutdown();
            while (proxyDoloadThreadExector.isTerminated()) {
                this.proxyDoloadThreadExector.shutdown();
            }
        }
        this.isContinue = false;
    }

}
