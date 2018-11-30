package com.example.ninomiya.rxjavainternet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class HtmlGet {
    static String htmlGet(String urls) {
        final String BR = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();

        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            // URL 文字列をセットします。
            URL url = new URL(urls);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000); // タイムアウト 3 秒
            connection.setReadTimeout(5000);

            // GET リクエストの実行
            connection.setRequestMethod("GET");
            connection.connect();


            // レスポンスコードの確認します。
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP responseCode: " + responseCode);
            }

            // 文字列化します。
            inputStream = connection.getInputStream();
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append(BR);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return sb.toString();
    }

    static String titleGet(String s) {
        if (s.contains("<title>")) {
            int i1 = s.indexOf("<title>") + 7;
            int i2 = s.indexOf("</title>");
            return s.substring(i1, i2);
        } else if (s.contains("<TITLE>")) {
            int i1 = s.indexOf("<TITLE>") + 7;
            int i2 = s.indexOf("</TITLE>");
            return s.substring(i1, i2);
        }
        return "skip";
    }
}
