package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gypsophila on 2016/3/18.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(address);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(8000);

                    StringBuilder sb = new StringBuilder();
                    String str;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while ((str = br.readLine()) != null) {
                        sb.append(str);
                    }
                    if (br != null) {
                        br.close();
                    }
                    listener.onFinish(sb.toString());

                }  catch (IOException e) {
                    //回调方法
                    listener.onError(e);
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
}
