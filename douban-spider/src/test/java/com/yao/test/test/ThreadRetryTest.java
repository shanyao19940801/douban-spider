package com.yao.test.test;

import com.yao.spider.core.http.client.AbstractHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class ThreadRetryTest extends AbstractHttpClient {
    private static Logger logger = LoggerFactory.getLogger(ThreadRetryTest.class);
    private static ThreadRetryTest instance;

    private ThreadPoolExecutor downLoadMoveListExector;


    public static volatile int MOVE_START = 0;

    public ThreadRetryTest() {
        init();
    }
    private void init () {

        downLoadMoveListExector = new ThreadPoolExecutor(100, 100, 0L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "downLoadMoveListExector " + r.hashCode());
                    }
                });


    }

    public static ThreadRetryTest getInstance() {
        if (instance == null) {
            synchronized (ThreadRetryTest.class) {
                if (instance == null) {
                    instance = new ThreadRetryTest();
                }
            }
        }
        return instance;
    }

    public ThreadPoolExecutor getDownLoadMoveListExector() {
        return downLoadMoveListExector;
    }


    public void setDownLoadMoveListExector(ThreadPoolExecutor downLoadMoveListExector) {
        this.downLoadMoveListExector = downLoadMoveListExector;
    }


    public void startDouBan() {
        new Thread(new ChuShiHuaTest(0)).start();
    }


}
