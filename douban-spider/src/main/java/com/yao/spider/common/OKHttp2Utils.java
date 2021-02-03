package com.yao.spider.common;


import com.squareup.okhttp.*;

import java.io.IOException;

public class OKHttp2Utils {

    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public static final String HTTP_200_STATUS = "200";

    public static String sendPost(String url, String param) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, param);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
//		if (response.isSuccessful()) {
//			String code = String.valueOf(response.code());
//			return StringUtils.isEmpty(code) ? HTTP_200_STATUS : code;
//		}
        return response.body().string();
    }
}
