package com.yao.test;

/**
 * Created by user on 2018/2/8.
 */
public class TryFinallYTest {
    public static void main(String[] args) {
        System.out.println(test());
    }

    public static String test(){
        try {
            testException();
            System.out.println("1");
            return "1";
        } catch (Exception e) {
            System.out.println("2");
            e.printStackTrace();
            return "2";
        } finally {
            System.out.println("3");
            return "3";
        }
//        return "";
    }

    public static void testException() throws Exception {
        throw new Exception("test");
    }
}
