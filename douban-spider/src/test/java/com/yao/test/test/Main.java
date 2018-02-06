package com.yao.test.test;

/**
 * Created by 单耀 on 2018/2/6.
 */
public class Main {
    public static void main(String[] args) {
//        new Thread(new ChuShiHuaTest(1)).start();
        ThreadRetryTest.getInstance().startDouBan();
    }

}
