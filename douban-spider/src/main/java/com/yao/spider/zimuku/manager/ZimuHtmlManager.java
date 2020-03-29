package com.yao.spider.zimuku.manager;

import com.yao.spider.core.util.MyBatiesUtils;
import com.yao.spider.zimuku.domain.ZimuFileInfo;
import com.yao.spider.zimuku.domain.ZimuHtml;
import com.yao.spider.zimuku.domain.ZimuInfo;
import com.yao.spider.zimuku.domain.ZimuInfoExtend;
import com.yao.spider.zimuku.parsers.ZimuParser;
import com.yao.spider.zimuku.service.ZimuFileInfoService;
import com.yao.spider.zimuku.service.ZimuHtmlService;
import com.yao.spider.zimuku.service.ZimuInfoExtendService;
import com.yao.spider.zimuku.service.ZimuInfoService;
import com.yao.spider.zimuku.service.impl.ZimuFileInfoServiceImpl;
import com.yao.spider.zimuku.service.impl.ZimuHtmlServiceImpl;
import com.yao.spider.zimuku.service.impl.ZimuInfoExtendServiceImpl;
import com.yao.spider.zimuku.service.impl.ZimuInfoServiceImpl;
import org.apache.ibatis.session.SqlSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

public class ZimuHtmlManager {
    private static String downLoadHost = "http://zmk.pw/";

    public static void insertDownloadUrl(String path) {
        try {
            SqlSession session = MyBatiesUtils.getSqlSession();
            File file = new File(path);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(inputReader);
            // 按行读取字符串
            String str;
            ZimuInfoExtendService service = new ZimuInfoExtendServiceImpl();
            while ((str = bf.readLine()) != null) {
                String[] split = str.split("@");
                try {
//                    service.insert(builderZimuInfoExtend(downLoadHost + split[0], Long.valueOf(split[1])), session);
                    ZimuInfoExtend zimuInfoExtend = builderZimuInfoExtend(downLoadHost + split[0], Long.valueOf(split[1]));
                    service.update(zimuInfoExtend, session);
                    System.out.println("update:" + zimuInfoExtend);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            bf.close();
            inputReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ZimuInfoExtend builderZimuInfoExtend(String value, Long zimuInfoId) {
        ZimuInfoExtend zimuInfoExtend = new ZimuInfoExtend();
        zimuInfoExtend.setZimuInfoId(zimuInfoId);
        zimuInfoExtend.setExtendValue(value);
        zimuInfoExtend.setExtendValueType(1);
        zimuInfoExtend.setRefType(1);
        return zimuInfoExtend;
    }

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

    /**
     * 根系html获取下载链接
     */
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

    /**
     * 获取真实的下载链接
     */
    public void getRealDownURL() {
        try {
            SqlSession session = MyBatiesUtils.getSqlSession();
            Long step = 10L;
            Long start = 7796l;
            Long end = start + step;
            ZimuInfoExtendService service = new ZimuInfoExtendServiceImpl();
            Long maxId = 9999L;
            StringBuilder builder = new StringBuilder();
            ZimuFileInfoService zimuInfoService = new ZimuFileInfoServiceImpl();
            while (maxId > start) {
                List<ZimuInfoExtend> zimuInfoExtends = service.selectByRange(start, end, session);
                for (ZimuInfoExtend zimuInfoExtend : zimuInfoExtends) {
                    try {
                        ZimuFileInfo fileInfo = downLoadZimu(zimuInfoExtend.getExtendValue());
                        if (fileInfo == null) continue;
                        fileInfo.setZimuId(zimuInfoExtend.getZimuInfoId());
                        fileInfo.setExtendId(zimuInfoExtend.getId());
                        zimuInfoService.isnert(fileInfo, session);
                        System.out.println(fileInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                start = end;
                end = start + step;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fixUrl() {
        try {
            SqlSession session = MyBatiesUtils.getSqlSession();
            Long step = 1000L;
            Long start = 1000L;
            Long end = start + step;
            ZimuInfoExtendService service = new ZimuInfoExtendServiceImpl();
            Long maxId = service.selectMaxId(session);
            StringBuilder builder = new StringBuilder();
            ZimuFileInfoService zimuInfoService = new ZimuFileInfoServiceImpl();
            while (maxId > start) {
                List<ZimuInfoExtend> zimuInfoExtends = service.selectByRange(start, end, session);
                for (ZimuInfoExtend zimuInfoExtend : zimuInfoExtends) {
                   String url = zimuInfoExtend.getExtendValue();
                   if (url.contains(""));
                }
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
        manager.getRealDownURL();
        insertDownloadUrl("F:\\视频剪辑\\字幕\\字幕文件下载.txt");
//        downLoadZimu("http://zmk.pw/download/MTI1MzM2fDAxNWM2YjUyYmY0NTE0YzUyMGFhYzVmOXwxNTg1MzI3ODg1fGEwNGI4MTk4fHJlbW90ZQ%3D%3D/svr/dx1");


    }

    private static Map<String, String> getHeaderMap() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Proxy-Connection", "keep-alive");
        header.put("Pragma", "no-cache");
        header.put("Upgrade-Insecure-Requests","1");
        header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header.put("Referer"," http://zmk.pw/dld/132680.html");
        header.put("Accept-Encoding","gzip, deflate");
        header.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8");
        header.put("Cookie","__cfduid=d87a15e98cef894a2fc39c1d129ce9d431584024960; __gads=ID=8ee10c0beefcb081:T=1584024961:S=ALNI_MbYBMpb46H1agrUzlTV27qheky8zw; PHPSESSID=rs0hlh4dpljnsio3d6053fc2t2");
        return header;
    }

    private static Map<String, String> getRealDownloadHeaderMap() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Accept-Encoding","gzip, deflate, br");
        header.put("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8");
        header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        header.put("Cookie","_ga=GA1.2.71384916.1584024919; _gid=GA1.2.894825036.1585149901; yunsuo_session_verify=2f6c49771d7181db32e9041c0e599866");
        header.put("Proxy-Connection", "keep-alive");
        header.put("Referer"," http://zmk.pw/dld/132680.html");
        header.put("Upgrade-Insecure-Requests","1");
        header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        header.put("sec-fetch-mode","navigate");
        header.put("sec-fetch-site","cross-site");
        header.put("sec-fetch-user","?1");
        return header;
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

    public static ZimuFileInfo downLoadZimu(String urlO) {
        if (urlO.contains("http:")) {
            urlO = urlO.replaceAll("http:", "https:");
        }
        try {
            URL url = new URL(urlO);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("GET");
            for (Entry<String, String> entry : getHeaderMap().entrySet()) {
                httpURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
            httpURLConnection.setFollowRedirects(false);
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 301) return null;
            String location = httpURLConnection.getHeaderField("Location");
            if (location.contains("http:")) {
                location = location.replaceAll("http:", "https:");
            }
            httpURLConnection.disconnect();

            URL urlD = new URL(location);
            HttpURLConnection downConn = (HttpURLConnection) urlD.openConnection();
            // 设定请求的方法，默认是GET
            downConn.setRequestMethod("GET");
            for (Entry<String, String> entry : getRealDownloadHeaderMap().entrySet()) {
                downConn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            downConn.connect();
            int responseCode2 = downConn.getResponseCode();
            if (responseCode2 != 200) return null;
            String fileHeader = downConn.getHeaderField("Content-Disposition");
            String[] fileSplit = fileHeader.split("=");
            String fileOriginName = new String(fileSplit[1].getBytes("ISO-8859-1"), "utf-8");
            fileOriginName.replaceAll("[zmk.pw]","");
            String fileRealName = fileOriginName.substring(1, fileOriginName.length() - 1);
            ZimuFileInfo fileInfo = buildFileBean(location, null, null, fileRealName);
            return fileInfo;
        } catch (IOException e) {

        }
        return null;
    }

    private static ZimuFileInfo buildFileBean(String url, Long zimuId, Long zimuExtendId, String fileName) {
        ZimuFileInfo fileInfo = new ZimuFileInfo();
        fileInfo.setExtendId(zimuExtendId);
        fileInfo.setZimuId(zimuId);
        fileInfo.setDownloadUrl(url);
        fileName = fileName.replaceAll("\\[zmk.pw\\]","");
        fileInfo.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
        fileInfo.setFileName(fileName);
        return fileInfo;
    }

}
