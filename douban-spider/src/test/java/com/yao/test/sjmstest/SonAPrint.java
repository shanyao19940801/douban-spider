package com.yao.test.sjmstest;

/**
 * Created by 单耀 on 2018/1/26.
 */
public class SonAPrint implements ParentPrint{
    public void print(String classname) {
        System.out.println("Clas A");
        System.out.println(classname);
    }
}
