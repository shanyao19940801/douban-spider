package com.yao.douban.douban.utils;

import com.yao.TestConsants;
import com.yao.spider.douban.utils.DBUtil;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by user on 2018/2/8.
 */
public class DBUtilTest extends TestCase {
    @Test
    public void testGetTypeList() throws Exception {
        DBUtil.getType("move");
    }

    public void testGetTypeMap() throws Exception {

        DBUtil.getTypeMap("move", TestConsants.context);
    }
}