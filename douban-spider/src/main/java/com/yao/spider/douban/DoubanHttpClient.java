package com.yao.spider.douban;

import com.yao.spider.douban.task.move.StartWithTypeTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 单耀 on 2018/1/30.
 */
public class DoubanHttpClient  {
    private static Logger logger = LoggerFactory.getLogger(DoubanHttpClient.class);
    private static DoubanHttpClient instance;

    private ThreadPoolExecutor downLoadMoveListExector;

    private ThreadPoolExecutor downLoadMoveInfoExector;

    public static volatile int MOVE_START = 0;

    public DoubanHttpClient() {
        init();
    }
    private void init () {

        downLoadMoveListExector = new ThreadPoolExecutor(100, 100, 0L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "downLoadMoveListExector " + r.hashCode());
                    }
                });

        downLoadMoveInfoExector = new ThreadPoolExecutor(100, 100, 0L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "downLoadMoveInfoExector " + r.hashCode());
                    }
                });
        //设置baohecelve
        downLoadMoveInfoExector.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        downLoadMoveListExector.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

    }

    public static DoubanHttpClient getInstance() {
        if (instance == null) {
            synchronized (DoubanHttpClient.class) {
                if (instance == null) {
                    instance = new DoubanHttpClient();
                }
            }
        }
        return instance;
    }

    public ThreadPoolExecutor getDownLoadMoveListExector() {
        return downLoadMoveListExector;
    }

    public ThreadPoolExecutor getDownLoadMoveInfoExector() {
        return downLoadMoveInfoExector;
    }

    public void setDownLoadMoveListExector(ThreadPoolExecutor downLoadMoveListExector) {
        this.downLoadMoveListExector = downLoadMoveListExector;
    }

    public void setDownLoadMoveInfoExector(ThreadPoolExecutor downLoadMoveInfoExector) {
        this.downLoadMoveInfoExector = downLoadMoveInfoExector;
    }

    public void startDouBan() {
//        new Thread(new SpiderDouBanInfo()).start();
        new Thread(new StartWithTypeTask()).start();
    }

    public void stopSpiderDouban(boolean isNoew) {
        if (isNoew) {
            this.downLoadMoveListExector.shutdownNow();
            this.downLoadMoveInfoExector.shutdownNow();
        } else {
            this.downLoadMoveListExector.shutdown();
            while (downLoadMoveListExector.isTerminated()) {
                downLoadMoveInfoExector.shutdown();
            }
        }
    }


}
