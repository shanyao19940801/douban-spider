package com.yao.test.serializable;

import com.yao.spider.proxytool.ProxyPool;
import com.yao.spider.proxytool.entity.Proxy;
import com.yao.spider.proxytool.proxyutil.MyIOutils;
import com.yao.spider.proxytool.proxyutil.ProxyConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;

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

        List<Proxy> proxyList = (List<Proxy>) MyIOutils.deserializeObject(ProxyConstants.PROXYSER_FILE_NMAE);
        ProxyPool.proxyQueue = new DelayQueue<Proxy>(proxyList);
    }
}
