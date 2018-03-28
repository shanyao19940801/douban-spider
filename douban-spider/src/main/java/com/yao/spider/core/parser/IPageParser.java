package com.yao.spider.core.parser;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/30.
 */
public interface IPageParser<T> {
    public List<T> parser(String html);
}
