package org.octocats.lecturenotegenerator;

/**
 * Created by ritwi on 08-10-2016.
 */

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;


public class summary {
    public String chut(String title, String text) throws Exception {
        final String BASE_URL = "https://api.aylien.com/api/v1/summarize";

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-AYLIEN-TextAPI-Application-Key","6521a17eaa666450c6e6f70f25924c96");
        client.addHeader("X-AYLIEN-TextAPI-Application-ID","f470587a");
        client.addHeader("Accept","application/json");
        RequestParams paramMap = new RequestParams();
        paramMap.put("title",title);
        paramMap.put("text",text);
        paramMap.put("sentences_percentage",30);



        return title;
    }
}
