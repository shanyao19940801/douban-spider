package com.yao.douban.proxytool.proxyutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by shanyao on 2018/3/26.
 */
public class MyIOutils {
    private static final Logger logger = LoggerFactory.getLogger(MyIOutils.class);
    public static void close(ObjectOutputStream outputStream) {
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化对象
     */
    public static void serializeObject(Object object, String fileName) {
        ObjectOutputStream oos = null;
        String path = ProxyConstants.FILE_PATH + "/" + fileName;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(object);
            oos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            MyIOutils.close(oos);
        }
    }

    /**
     * 放序列化对象
     * @param fileName 文件名，改名称必须在resources/file下面
     */
    public static void deserializeObject(String fileName) {
        
    }
}
