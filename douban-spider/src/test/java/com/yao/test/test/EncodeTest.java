package com.yao.test.test;

import com.yao.douban.core.util.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by 单耀 on 2018/2/5.
 */
public class EncodeTest {
    public static void main(String[] args) {
        String str = "%E8%B1%86%E7%93%A3%E9%AB%98%E5%88%86";
        try {
            String re = URLDecoder.decode(str,"UTF-8");
            System.out.println(re);
            String ss = URLEncoder.encode(Constants.TAG,"UTF-8");
            System.out.println(ss);
            if (ss.equals(str)) {
                System.out.println("==");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
