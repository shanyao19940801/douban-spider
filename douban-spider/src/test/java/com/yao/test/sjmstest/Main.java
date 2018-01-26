package com.yao.test.sjmstest;

/**
 * Created by 单耀 on 2018/1/26.
 */
public class Main {
    public static void main(String[] args) {
        ParentPrint print = PrintFactory.getParentPrint(SonAPrint.class);
        print.print("A");

        ParentPrint print1 = PrintFactory.getParentPrint(SonBPrint.class);
        print.print("B");
    }
}
