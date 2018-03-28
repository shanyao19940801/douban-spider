package com.yao.spider.douban.parsers.move;

import com.yao.spider.douban.entity.move.MoveList;
import com.yao.spider.douban.parsers.DoubanPageParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 单耀 on 2018/2/4.
 */
@Deprecated
public class MoveListParser implements DoubanPageParser<MoveList> {
    private static Logger logger = LoggerFactory.getLogger(MoveListParser.class);
    public List<MoveList> parser(String html) {
        try {
            JSONObject jsonObject = JSONObject.fromObject(html);
            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
            List<MoveList> move1s = (List<MoveList>) JSONArray.toCollection(jsonArray, MoveList.class);
            return move1s;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
