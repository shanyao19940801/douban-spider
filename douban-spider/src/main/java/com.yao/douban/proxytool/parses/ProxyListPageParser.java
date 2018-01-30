package com.yao.douban.proxytool.parses;

import com.yao.douban.proxytool.entity.Proxy;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/27.
 */
public interface ProxyListPageParser {
    /**
     * 是否只需要匿名代理
     */
    boolean anonymousFlag = false;

    List<Proxy> parse(String html);
}
