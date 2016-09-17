package com.ilfalsodemetrio.handlers;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.net.URLEncoder;

/**
 * Created by lbrtz on 08/09/16.
 */
public class WikipediaHandler {
    /**
     * Search wikipedia
     *
     * @param term
     * @param lang
     * @return
     */
    public static String process(String term, String lang, String nothing) {
        final String ENDPOINT = "https://"+lang+".wikipedia.org/w/api.php";

        String output = nothing;
        CloseableHttpClient httpClient =
                HttpClientBuilder.create().build();

        try {
            String param = URLEncoder.encode(term.trim().toLowerCase(), "utf-8");
            HttpGet request = new HttpGet(ENDPOINT+"?action=opensearch&limit=2&search="+param);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(entity);
            String responseText = EntityUtils.toString(buf);
            JSONArray jsonArray = new JSONArray(responseText);
            if (jsonArray != null && jsonArray.getJSONArray(2) != null && jsonArray.getJSONArray(2).length() > 0) {
                output = jsonArray.getJSONArray(2).get(0).toString();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }
}
