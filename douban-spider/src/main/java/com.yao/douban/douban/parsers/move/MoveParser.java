package com.yao.douban.douban.parsers.move;

import com.yao.douban.douban.entity.move.ListMove;
import com.yao.douban.douban.entity.move.Move;
import com.yao.douban.douban.parsers.DoubanPageParser;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by 单耀 on 2018/2/8.
 */
public class MoveParser implements DoubanPageParser<Move> {
    private static Logger logger = LoggerFactory.getLogger(MoveParser.class);
    public List<Move> parser(String html) {
        try {
            JSONArray jsonArray = JSONArray.fromObject(html);
//            JSONObject jsonObject = JSONObject.fromObject(html);
//            JSONArray jsonArray = jsonObject.getJSONArray("subjects");
            List<Move> listMoves = (List<Move>) JSONArray.toCollection(jsonArray, Move.class);
            return listMoves;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
