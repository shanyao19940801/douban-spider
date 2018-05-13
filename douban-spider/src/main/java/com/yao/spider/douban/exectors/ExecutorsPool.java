package com.yao.spider.douban.exectors;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by shanyao on 2018/5/13.
 */
public class ExecutorsPool { //TODO 这样写好像不行？

    private ThreadPoolExecutor downLoadMoveListExector;
    private ThreadPoolExecutor downLoadMoveInfoExector;

    public ThreadPoolExecutor getDownLoadMoveListExector() {
        return downLoadMoveListExector;
    }

    public void setDownLoadMoveListExector(ThreadPoolExecutor downLoadMoveListExector) {
        this.downLoadMoveListExector = downLoadMoveListExector;
    }

    public ThreadPoolExecutor getDownLoadMoveInfoExector() {
        return downLoadMoveInfoExector;
    }

    public void setDownLoadMoveInfoExector(ThreadPoolExecutor downLoadMoveInfoExector) {
        this.downLoadMoveInfoExector = downLoadMoveInfoExector;
    }
}
