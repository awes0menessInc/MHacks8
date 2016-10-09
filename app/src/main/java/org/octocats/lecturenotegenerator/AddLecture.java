package org.octocats.lecturenotegenerator;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.content.ContentValues.TAG;


/**
 * Created by nisarg on 8/10/16.
 */
public class AddLecture extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_TAKE_GALLERY_VIDEO = 2;

    private EditText title;
    private String titleTxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lec);

        ImageButton uploadBtn = (ImageButton) findViewById(R.id.upload);
        ImageButton recordBtn = (ImageButton) findViewById(R.id.record);

        title = (EditText) findViewById(R.id.title);
        titleTxt = ""+title.getText();

        recordBtn.setOnClickListener(new View.OnClickListener()
        {
            String videoPath;
            public void onClick(View view)
            {
                //User can take a video from here
                //after accepting, the video is stored in the dcim of the user's local storage
                //videoPath String stores the path of the video, which can be accessed in the clicklistener method.
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
            }


        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);


            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,data.getDataString());
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                final String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);
                if (selectedImagePath != null) {

                    /*Intent intent = new Intent(HomeActivity.this,
                            VideoplayAvtivity.class);
                    intent.putExtra("path", selectedImagePath);
                    startActivity(intent);*/
                    File f = new File(selectedImagePath);
                    String imageName = f.getName();
                    Log.e(TAG,"lk"+imageName);

                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();
                    try {
                        params.put("userPhoto", f);
                    } catch(FileNotFoundException e) {}

                    client.post("http://nisarg.me:3000/api/photo", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String str = "";
                            try {
                                str = new String(responseBody, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            JSONObject json = new JSONObject();
                            try {
                                json = new JSONObject(str);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String filename = "";
                            try {
                                Log.e(TAG, "success " + json.getString("filename"));
                                filename = json.getString("filename");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            AsyncHttpClient client = new AsyncHttpClient();
                            client.addHeader("Content-Type","application/json");
                            RequestParams params = new RequestParams();

                            JSONObject jsonParam = new JSONObject();
                            try {
                                jsonParam.put("action", "index_content");
                                jsonParam.put("userID", "1475952683-484e4700-3a91-4ee3-8344-125b3f259469-8693430633199653278155461417427");
                                jsonParam.put("data_url", "http://nisarg.me:3000/uploads/" + filename);
                                StringEntity entity = new StringEntity(jsonParam.toString());
                                Log.e(TAG, params.toString());

                                client.post(getApplicationContext(), "http://api.deepgram.com/", entity, "application/json", new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Log.e(TAG, response.toString());

                                        AsyncHttpClient client = new AsyncHttpClient();
                                        JSONObject jsonRes = null;
                                        try {
                                            jsonRes = new JSONObject(response.toString());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        JSONObject jsonParam = new JSONObject();
                                        StringEntity entity = null;
                                        try {
                                            jsonParam.put("action", "get_object_status");
                                            jsonParam.put("userID", "1475952683-484e4700-3a91-4ee3-8344-125b3f259469-8693430633199653278155461417427");
                                            jsonParam.put("contentID", jsonRes.getString("contentID"));
                                            entity = new StringEntity(jsonParam.toString());
                                            final String contentID  = jsonRes.getString("contentID");

                                            checkAPI(contentID);

                                        } catch (Exception e){
                                            Log.e(TAG, e.toString());
                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                        Log.e(TAG, "" + statusCode);
                                        Log.e(TAG, res.toString());

                                    }
                                });
                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            String str = "";

                            Log.e(TAG, "fail " + error.toString());
                        }
                    });
                }
            } else if(requestCode == REQUEST_VIDEO_CAPTURE){
                String videoPath = getPath(data.getData());
                File f = new File(videoPath);
                String imageName = f.getName();
                Log.e(TAG,imageName);



            }
        }
    }


    public void checkAPI(String contentID) throws JSONException, UnsupportedEncodingException {
        AsyncHttpClient client = new AsyncHttpClient();

        JSONObject jsonParam = new JSONObject();
        StringEntity entity = null;

        jsonParam.put("action", "get_object_status");
        jsonParam.put("userID", "1475952683-484e4700-3a91-4ee3-8344-125b3f259469-8693430633199653278155461417427");
        jsonParam.put("contentID", contentID);
        entity = new StringEntity(jsonParam.toString());

        final String contentIDCopy = contentID;

        client.post(getApplicationContext(), "http://api.deepgram.com/", entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    if (!response.getString("status").equals("done")) {
                        TimeUnit.SECONDS.sleep(10);
                        checkAPI(contentIDCopy);
                    } else {
                        getTranscript(contentIDCopy);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG, "" + statusCode);
                Log.e(TAG, res.toString());

            }
        });

    }

    public void getTranscript(String contentID) throws JSONException, UnsupportedEncodingException {
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("action", "get_object_transcript");
        jsonParam.put("userID", "1475952683-484e4700-3a91-4ee3-8344-125b3f259469-8693430633199653278155461417427");
        jsonParam.put("contentID", contentID);
        StringEntity entity = new StringEntity(jsonParam.toString());

        client.post(getApplicationContext(), "http://api.deepgram.com/", entity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String txt = "";
                    for(int i = 0; i < response.getJSONArray("paragraphs").length(); i++){
                        txt += response.getJSONArray("paragraphs").get(i).toString();
                        txt += "\n";
                    }
                    getSummary(titleTxt, txt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e(TAG, "" + statusCode);
                Log.e(TAG, res.toString());

            }
        });
    }

    /*public boolean checkStatus(final AsyncHttpClient client, final JSONObject jsonRes) {
        JSONObject jsonParam = new JSONObject();
        StringEntity entity = null;
        try {
            jsonParam.put("action", "get_object_status");
            jsonParam.put("userID", "1475952683-484e4700-3a91-4ee3-8344-125b3f259469-8693430633199653278155461417427");
            jsonParam.put("contentID", jsonRes.getString("contentID"));
            entity = new StringEntity(jsonParam.toString());
            final Boolean[] done = new Boolean[1];


            client.post(getApplicationContext(), "http://api.deepgram.com/", entity, "application/json", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.e(TAG, response.toString());
                    try {
                        if (response.getString("status").equals("")) {
                            TimeUnit.SECONDS.sleep(10);
                            if (checkStatus(client, jsonRes));
                        } else {
                            done[0] = new Boolean(true);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.e(TAG, "" + statusCode);
                    Log.e(TAG, res.toString());

                }
            });
            return done[0];
        } catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
    */

    public String getSummary(String title, String text)/* throws Exception */{
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
                Log.e(TAG, "success" + str);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "failure" + str);
            }
        });



        return val[0];
    }

    // UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
}
