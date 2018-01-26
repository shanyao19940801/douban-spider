package com.yao.test.sjmstest;

/**
 * Created by 单耀 on 2018/1/26.
 */
public class SonBPrint implements ParentPrint {
    public void print(String classname) {
        System.out.println("Class B");
        System.out.println(classname);
    }
}
