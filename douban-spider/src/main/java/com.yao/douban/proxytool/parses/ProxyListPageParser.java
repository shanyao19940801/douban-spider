package com.yao.douban.proxytool.parses;

import com.yao.douban.proxytool.entity.Proxy;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/27.
 */
public interface ProxyListPageParser {
    boolean anonymousFlag = false;

    List<Proxy> parse(String html);
}
