package com.yao.test.logtest;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 单耀 on 2018/2/8.
 */
public class SonLog extends AbstractLog {
    private static Logger logger= LoggerFactory.getLogger(SonLog.class);
    @Test
    public void printLogSon() {
        logger.info("son");
        printLog();
    }
}
