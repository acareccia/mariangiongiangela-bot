package com.ilfalsodemetrio.handlers;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.net.URLEncoder;

/**
 * Created by lbrtz on 08/09/16.
 */
public class WikipediaHandler {
    private static final String BASE_URL="https://it.wikipedia.org/api/rest_v1/page/summary/";

    public static String process(String term, String nothing) {
        String output = nothing;

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            String param = URLEncoder.encode(term.trim().toLowerCase(), "utf-8");
            HttpGet request = new HttpGet(BASE_URL + param);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(entity.getContent()));

            output = (String)jsonObject.get("extract");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
