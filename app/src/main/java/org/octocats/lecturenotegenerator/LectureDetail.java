package org.octocats.lecturenotegenerator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.AbsoluteLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Utkarsh on 10/8/2016.
 */

public class LectureDetail extends Activity
{
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private SumListAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturedetailactivity);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("URI");
        JSONObject textSummary = new JSONObject();
        try {
           textSummary = new JSONObject(extras.getString("TEXT_SUMMARY"));
            Log.e(TAG, textSummary.toString());
        } catch (JSONException e) {e.printStackTrace();}




        VideoView vid = (VideoView) findViewById(R.id.videoView);
        vid.setVideoURI(Uri.parse(value));
        //vid.setMediaController(new MediaController(this));
        vid.start();



        JSONArray sentences = new JSONArray();
        try {
            sentences = textSummary.getJSONArray("sentences");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayList<String> sums = new ArrayList<String>();
        if (sentences != null) {
            for (int i=0;i<sentences.length();i++){
                try {
                    sums.add(sentences.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.sumList);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mAdapter = new SumListAdapter(sums, new ArrayList<String>());
        mRecyclerView.setAdapter(mAdapter);



    }

}
