package com.yao.test.serializable;

import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.proxyutil.MyIOutils;
import sun.misc.IOUtils;

import java.io.*;

/**
 * Created by shanyao on 2018/3/26.
 */
public class ProxySerializable {
    public static void main(String[] args) {
        Proxy p1 = new Proxy("001",90);
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream("F:\\proects\\douban\\proxy.ser"));
            outputStream.writeObject(p1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            MyIOutils.close(outputStream);
        }

        //Read object from file
        File file = new File("F:\\proects\\douban\\proxy.ser");
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
            Proxy proxy = (Proxy) inputStream.readObject();
            System.out.printf(proxy.getProxyStr());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
