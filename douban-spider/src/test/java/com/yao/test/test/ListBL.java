package com.yao.test.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by shanyao on 2018/4/2.
 */
public class ListBL {
    public static void main(String[] args) {
        ListBL listBL = new ListBL();
        listBL.testMain();
    }
    public void testMain(){
        //初始化
        int sum = 10000;
        List<String> arrList = new ArrayList<String>();
        List<String> linkList = new LinkedList<String>();
        String con = "test";
        for(int i=0;i<sum;i++){
            arrList.add(con);
            linkList.add(con);
        }
        System.out.println("---------------------测试结果------------------------");
        System.out.println("for      arrList 时间是 " + testFor(arrList));
//        System.out.println("for linkList 时间是 " + testFor(linkList));
        System.out.println("for    arrList01 时间是 " + testFor01(arrList));
//        System.out.println("for linkList01 时间是 " + testFor01(linkList));
        System.out.println("for    arrList02 时间是 " + testFor02(arrList));
//        System.out.println("for linkList02 时间是 " + testFor02(linkList));
        System.out.println("iterator arrList 时间是 " + testIterator(arrList));
//        System.out.println("iterator linkList 时间是 " + testIterator(linkList));
    }


    public long testFor(List<String> list) {
        long startTime = 0L,endTime = 0L;
        String str;
        startTime = System.nanoTime();
        for(int i=list.size() - 1; i>=0; i--){
            str = list.get(i);
        }
        endTime = System.nanoTime();
        return endTime - startTime;
    }
    public long testFor02(List<String> list) {
        long startTime = 0L,endTime = 0L;
        String str;
        startTime = System.nanoTime();
        for(int i=0,len = list.size(); i<len; i++){
            str = list.get(i);
        }
        endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long testFor01(List<String> list) {
        long startTime = 0L,endTime = 0L;
        String str;
        startTime = System.nanoTime();
        for(String str1 : list){
            str = str1;
        }
        endTime = System.nanoTime();
        return endTime - startTime;
    }


    public long testIterator(List<String> list){
        long startTime = 0L, endTime = 0L;
        String str;
        startTime = System.nanoTime();
        Iterator<String> it = list.iterator();
        while(it.hasNext()){
            str = it.next();
        }
        endTime = System.nanoTime();
        return endTime - startTime;

    }
}
