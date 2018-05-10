package com.yao.test;

import com.yao.spider.core.task.AbstractTask;
import com.yao.spider.zhihu.task.ZhiHuUserListTask;

/**
 * Created by shanyao on 2018/5/10.
 */
public class ContractTest {
    public static void main(String[] args) {
        ZhiHuUserListTask abstractTask = new ZhiHuUserListTask("test",false);
    }
}
