package com.yao.douban.proxytool.proxyutil;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by shanyao on 2018/3/26.
 */
public class MyIOutils {
    public static void close(ObjectOutputStream outputStream) {
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
