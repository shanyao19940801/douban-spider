package com.yao.test.test;

/**
 * Created by 单耀 on 2018/1/27.
 */
public class ExceptionTest {
    public static void main(String[] args) {
        ExceptionTest test = new ExceptionTest();
        try {
            test.method();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void method() throws Exception {
        int i =1;
        float j = i/0;
    }
}
