package com.yao.plan;

import java.util.*;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * shutdown时保存还未开始执行的任务
 * Created by shanyao on 2018/3/19.
 * 这个自己封装的Executor，
 * 当线程池shutdown就可以记录，没有执行的任务，以便下次运行项目时继续执行
 */
public class TrackingExecutor extends AbstractExecutorService{
    private final ExecutorService exec;
    private final Set<Runnable> tasksCancelledShudown = Collections.synchronizedSet(new HashSet<Runnable>());

    public TrackingExecutor(ExecutorService exec) {
        this.exec = exec;
    }

    public void shutdown() {

    }

    public List<Runnable> shutdownNow() {
        return null;
    }

    public boolean isShutdown() {
        return false;
    }

    public boolean isTerminated() {
        return false;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void execute(final Runnable runnable) {
        exec.execute(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } finally {
                    if (isShutdown() && Thread.currentThread().isInterrupted()) {
                        tasksCancelledShudown.add(runnable);
                    }
                }
            }
        });
    }

    public List<Runnable> getCancelledTasks() {
        if (!exec.isTerminated()) {
            throw new IllegalStateException("");
        }
        return new ArrayList<Runnable>(tasksCancelledShudown);
    }


}
