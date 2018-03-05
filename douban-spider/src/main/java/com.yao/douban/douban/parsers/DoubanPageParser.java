package com.yao.douban.douban.parsers;

import java.util.List;

/**
 * Created by 单耀 on 2018/1/30.
 */
public interface DoubanPageParser<T> {
//    public List<MoveDeprecated> parser(String html);
    public List<T> parser(String html);
}
