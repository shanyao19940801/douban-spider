package com.yao.test.test;

/**
 * Created by 单耀 on 2018/2/6.
 */
public class ChuShiHuaTest implements Runnable{
    private int retryTime = 0;

    public ChuShiHuaTest(int retryTime) {
        this.retryTime = retryTime;
    }


    public void run() {
        System.out.println(retryTime);
        retry();
    }

    private void retry() {
        System.out.println(retryTime);
        ThreadRetryTest.getInstance().getDownLoadMoveListExector().execute(new ChuShiHuaTest(retryTime +1));
    }
}
