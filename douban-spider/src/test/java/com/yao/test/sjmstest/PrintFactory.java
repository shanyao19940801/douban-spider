package com.yao.test.sjmstest;

/**
 * Created by 单耀 on 2018/1/26.
 */
public class PrintFactory {
    public static ParentPrint getParentPrint(Class clazz) {
        try {
            ParentPrint print = (ParentPrint) clazz.newInstance();
            return print;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
