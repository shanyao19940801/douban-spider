package com.yao.spider.zimuku.manager;

import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.parser.IPageParser;
import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.zimuku.domain.ZimuHtml;
import com.yao.spider.zimuku.domain.ZimuInfo;
import com.yao.spider.zimuku.parsers.ZimuParser;
import com.yao.spider.zimuku.service.ZimuHtmlService;
import com.yao.spider.zimuku.service.ZimuInfoService;
import com.yao.spider.zimuku.service.impl.ZimuHtmlServiceImpl;
import com.yao.spider.zimuku.service.impl.ZimuInfoServiceImpl;
import org.apache.ibatis.session.SqlSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ZimuHtmlManager {

    public static void batchInsert(String path) {
        try {
            SqlSession session = MyBatiesUtils.getSqlSession();
            File file = new File(path);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            // 按行读取字符串
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = bf.readLine()) != null) {
                if ("#####".equals(str)) {
                    ZimuHtmlService service = new ZimuHtmlServiceImpl();
                    service.insert(builderHtml(builder.toString()), session);
                    builder = new StringBuilder();
                    continue;
                }
                builder.append(str);
            }
            bf.close();
            inputReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void jsoupHtml() {
        try {
            SqlSession session = MyBatiesUtils.getSqlSession();
            Long step = 1000L;
            Long start = 1000L;
            Long end = start + step;
            ZimuHtmlService service = new ZimuHtmlServiceImpl();
            Long maxId = service.selectMaxId(session);
            StringBuilder builder = new StringBuilder();
            String str;
            ZimuParser parser = new ZimuParser();
            ZimuInfoService zimuInfoService = new ZimuInfoServiceImpl();
            while (maxId > start) {
                List<ZimuHtml> zimuHtmls = service.selectByRange(start, end, session);
                List<ZimuInfo> zimuInfos = new ArrayList<ZimuInfo>(zimuHtmls.size());
                for (ZimuHtml zimuHtml : zimuHtmls) {
                    ZimuInfo zimuInfo = ZimuParser.getBeanWithHtml(zimuHtml.getHtmlValue());
                    zimuInfo.setHtmlId(zimuHtml.getId());
                    zimuInfos.add(zimuInfo);
                }
                zimuInfoService.batchInsert(zimuInfos, session);
                start = end;
                end += start + step;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ZimuHtml builderHtml(String string) {
        ZimuHtml html = new ZimuHtml();
        html.setHtmlType(1);
        html.setHtmlValue(string);
        return html;
    }

    public static void main(String[] args) {
        ZimuHtmlManager manager = new ZimuHtmlManager();
        manager.jsoupHtml();
//        manager.batchInsert("F:\\work\\myproject\\douban-spider\\单耀火车头字幕下载.txt");

    }

}
