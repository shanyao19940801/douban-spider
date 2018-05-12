package com.yao.test;

import com.yao.spider.core.task.AbstractTask;
import com.yao.spider.zhihu.task.ZhiHuUserListTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shanyao on 2018/5/10.
 */
public class ContractTest {
    static Logger logger = LoggerFactory.getLogger(ContractTest.class);
    public static void main(String[] args) {
//        ZhiHuUserListTask abstractTask = new ZhiHuUserListTask("test",false);
        ContractTest test = new ContractTest();
    }

    static {
        System.out.println("1");
    }


}
