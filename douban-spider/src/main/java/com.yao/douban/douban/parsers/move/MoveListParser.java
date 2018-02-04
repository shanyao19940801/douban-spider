package com.yao.douban.douban.parsers.move;

import com.yao.douban.douban.entity.move.ListMove;
import com.yao.douban.douban.parsers.DoubanPageParser;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 单耀 on 2018/2/4.
 */
public class MoveListParser implements DoubanPageParser<ListMove>{
    private static Logger logger = LoggerFactory.getLogger(MoveListParser.class);
    public List<ListMove> parser(String html) {
        try {
            JSONObject jsonObject = JSONObject.fromObject(html);
            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
            List<ListMove> listMoves = (List<ListMove>) JSONArray.toCollection(jsonArray, ListMove.class);
            return listMoves;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
