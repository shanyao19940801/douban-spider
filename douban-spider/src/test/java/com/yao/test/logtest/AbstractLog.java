package com.yao.test.logtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 单耀 on 2018/2/8.
 */
public abstract class AbstractLog {
    private static Logger logger = LoggerFactory.getLogger(AbstractLog.class);

    public void printLog(){
        logger.info("parent");
    }
}
