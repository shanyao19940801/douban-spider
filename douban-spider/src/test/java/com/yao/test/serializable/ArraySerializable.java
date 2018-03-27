package com.yao.test.serializable;

import com.yao.douban.proxytool.ProxyHttpClient;
import com.yao.douban.proxytool.entity.Proxy;
import com.yao.douban.proxytool.proxyutil.MyIOutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanyao on 2018/3/26.
 * 关于ArrayList内部实这个讲的很不错
 * http://www.importnew.com/18024.html
 */
public class ArraySerializable {
    public static void main(String[] args) {
        List<Proxy> list = new ArrayList<Proxy>();
        Proxy proxy = new Proxy("1",1);
        list.add(proxy);
        MyIOutils.serializeObject(list,"test.ser");

        List<Proxy> proxies = (List<Proxy>) MyIOutils.deserializeObject("test.ser");
        System.out.println(proxies.size());
    }
}
