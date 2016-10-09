package org.octocats.lecturenotegenerator;

/**
 * Created by ritwi on 08-10-2016.
 */

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;


public class summary {
    String tag = "lecturenotegenerator";
    public String chut(String title, String text)/* throws Exception */{
        final String BASE_URL = "https://api.aylien.com/api/v1/summarize";
        final String val[] = new String[1];
        val[0] = new String();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-AYLIEN-TextAPI-Application-Key","6521a17eaa666450c6e6f70f25924c96");
        client.addHeader("X-AYLIEN-TextAPI-Application-ID","f470587a");
        client.addHeader("Accept","application/json");
        RequestParams paramMap = new RequestParams();
        paramMap.put("title",title);
        paramMap.put("text",text);
        paramMap.put("sentences_percentage",30);

        client.post(BASE_URL, paramMap, new AsyncHttpResponseHandler() {
            String str;
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    str = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                val[0] = str;
                Log.e(tag, "success" + str);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(tag, "failure" + str);
            }
        });



        return val[0];
    }

}
