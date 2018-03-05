package com.yao.douban.douban.parsers.move;

import com.yao.douban.douban.entity.move.MoveDeprecated;
import com.yao.douban.douban.parsers.DoubanPageParser;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/2/9.
 */
public class MoveParser implements DoubanPageParser{
    private static Logger logger = LoggerFactory.getLogger(MoveParser.class);

    public List<MoveDeprecated> parser(String html) {
        try {
            String[] excludes = new String[]{"rating", "rank","cover_url","cover_url"};
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setExcludes(excludes);
            JSONArray jsonArray = JSONArray.fromObject(html, jsonConfig);
            List<MoveDeprecated> list = (List<MoveDeprecated>) jsonArray.toCollection(jsonArray, MoveDeprecated.class);
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ArrayList<MoveDeprecated>();
    }
}
