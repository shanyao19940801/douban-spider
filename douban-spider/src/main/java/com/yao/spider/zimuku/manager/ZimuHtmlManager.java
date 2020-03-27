package com.yao.spider.zimuku.manager;

import com.yao.spider.core.factory.ParserFactory;
import com.yao.spider.core.http.HttpClientManagerV2;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

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
       /* ZimuHtmlManager manager = new ZimuHtmlManager();
        manager.jsoupHtml();*/
//        manager.batchInsert("F:\\work\\myproject\\douban-spider\\单耀火车头字幕下载.txt");
        Set<String> excludeProxyHosts = Collections.emptySet();
        HttpClientManagerV2 httpClientManager = new HttpClientManagerV2(5000, 15000, excludeProxyHosts, null, null);
        String url = "http://zmk.pw/download/MTMyNjgwfDQ0NWYzZWM4ZDE2MmQ4ZTVjZWFiMWNiZHwxNTg1Mjk2NjgyfGQ5NmY2NWQ1fHJlbW90ZQ%3D%3D/svr/dx1";
        String res = httpClientManager.execGetRequestWithContent(url, "__gads=ID=216dd2a9751ae704:T=1585279338:S=ALNI_Mb0MDGv4FD5PRGpxQQt3EJBsBlhLg; __cfduid=d992e7b7177c5fbd83f53f414d0c29bb91585279337; PHPSESSID=qsdrehmfpr0jmfsqhi6rf4h1f5");
        System.out.println(res);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Proxy-Connection", "keep-alive");
        header.put("Pragma", "no-cache");
        header.put("Upgrade-Insecure-Requests","1");
        header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        header.put("Referer"," http://zmk.pw/dld/132680.html");
        header.put("Accept-Encoding","gzip, deflate");
        header.put("Accept-Language"," zh-CN,zh;q=0.9");
        header.put("Cookie","__cfduid=d444be9233f69d023b7072eb6b9164bda1585117374; __gads=ID=050a8933b4c6445e:T=1585117375:S=ALNI_MbwPHLv91M3n0-auRGmR4Psetl7oA; PHPSESSID=e0so1hqf03a8b517gcppu681p4");

//        InputStream inputStream = httpClientManager.execGetInRequestWithHeader(url, header);
        String stringRes = httpClientManager.execGetRequestWithHeader(url, header);
        InputStream inputStream = new ByteArrayInputStream(stringRes.getBytes());


        try {
            //得到输入流
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            String s = bytesToHexString(getData);
            System.out.println(s);
            //文件保存位置
            File saveDir = new File("E:\\my_git\\douban-spider");
            if(!saveDir.exists()){
                saveDir.mkdir();
            }
            File file = new File(saveDir+File.separator+"aaaaa");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
