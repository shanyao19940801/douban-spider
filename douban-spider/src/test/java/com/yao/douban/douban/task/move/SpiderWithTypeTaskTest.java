package com.yao.douban.douban.task.move;

import com.yao.TestConsants;
import com.yao.douban.douban.task.AbstractTask;
import com.yao.douban.douban.utils.DBUtil;
import junit.framework.TestCase;

import java.util.Map;

/**
 * Created by user on 2018/2/8.
 */
public class SpiderWithTypeTaskTest extends TestCase {

    public void testRun() throws Exception {
        Map<String,String> map = DBUtil.getTypeMap("move", TestConsants.context);
//        for (String key : map.keySet()) {
        AbstractTask task = new SpiderWithTypeTask("剧情", "11", false);
        task.run();
//        new Thread(new SpiderWithTypeTask("剧情", map.get("11"), false)).start();
//        }
    }
}