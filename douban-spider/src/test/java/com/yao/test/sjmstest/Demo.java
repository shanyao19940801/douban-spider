package com.yao.test.sjmstest;

/**
 * Created by 单耀 on 2018/1/26.
 */
public class Demo {
    public static void main(String[] args) {
        try {
            /**
             *  Class.forName(String):要求JVM查找并加载String指定的类
             *  返回String串指定的类
             */
            Class clazz = Class.forName("com.yao.test.sjmstest.Demo");
            /**
             * clazz.newInstance()
             * 返回的类所代表的一个实例和new Demo()效果是一样的。
             */
            Demo demo = (Demo) clazz.newInstance();
            demo.method(demo);
            //这里的demo1与上面的demo效果是一样的
            Demo demo1 = new Demo();
            demo1.method(demo1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void method(Demo demo) {
        System.out.println(demo);
    }
}
