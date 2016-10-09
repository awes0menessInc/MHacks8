package org.octocats.lecturenotegenerator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

import static android.content.ContentValues.TAG;

/**
 * Created by Utkarsh on 10/8/2016.
 */

public class LectureDetail extends Activity
{
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private SumListAdapter mAdapter;
    private VideoView vid;
    ArrayList<String> sums = new ArrayList<>();

    HashMap<Integer,String> times = new HashMap<>();

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturedetailactivity);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("URI");
        String contentID = extras.getString("CONTENT_ID");


        JSONObject textSummary = new JSONObject();
        try {
           textSummary = new JSONObject(extras.getString("TEXT_SUMMARY"));
            Log.e(TAG, textSummary.toString());
        } catch (JSONException e) {e.printStackTrace();}




        vid = (VideoView) findViewById(R.id.videoView);
        vid.setVideoURI(Uri.parse(value));
        vid.setMediaController(new MediaController(this));
        vid.start();



        JSONArray sentences = new JSONArray();


        try {
            sentences = textSummary.getJSONArray("sentences");
            sums = new ArrayList<String>();
            if (sentences != null) {
                for (int i=0;i<sentences.length();i++){
                    sums.add(sentences.getString(i));

                    final int j = i;
                    try {
                        AsyncHttpClient client = new AsyncHttpClient();
                        JSONObject jsonParam = new JSONObject();

                        JSONObject filter = new JSONObject();
                        filter.put("Nmax", 10);
                        filter.put("Pmin", 0.55);
                        jsonParam.put("action", "object_search");
                        jsonParam.put("userID", "1476010302-f9f306e3-773b-4252-832e-dd09172f02f4-1946553840890124384591255084455");
                        jsonParam.put("contentID", contentID);
                        jsonParam.put("query", sentences.getString(i));
                        jsonParam.put("filter", filter);
                        jsonParam.put("sort", "time");
                        StringEntity entity = new StringEntity(jsonParam.toString());
                        client.post(getApplicationContext(), "http://api.deepgram.com/", entity, "application/json", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                try {
                                    times.put(j,""+response.getJSONArray("startTime").get(0));
                                    try {
                                        Log.e(TAG, "" + times.toString());
                                    } catch (Exception e){
                                        Log.e(TAG, e.toString());
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, e.toString());
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

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e){
                        e.printStackTrace();
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        mRecyclerView = (RecyclerView) findViewById(R.id.sumList);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mAdapter = new SumListAdapter(sums, contentID, times);
        mAdapter.setOnItemClickListener(onItemClickListener);
        mRecyclerView.setAdapter(mAdapter);


    }

    private final SumListAdapter.OnItemClickListener onItemClickListener = new SumListAdapter.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onItemClick(View v, int position) {
            try {
                if (times.get(position) != null) {
                    vid.seekTo((int) Double.parseDouble(times.get(position)) * 1000);
                }
                Log.e(TAG, "clicked " + position);
            } catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }
    };




}
