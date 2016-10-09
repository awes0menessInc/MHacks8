package org.octocats.lecturenotegenerator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Utkarsh on 10/8/2016.
 */

public class LectureDetail extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecturedetailactivity);

        Bundle extras = getIntent().getExtras();
        String value = extras.getString("URI");


        VideoView vid = (VideoView) findViewById(R.id.videoView);
        vid.setVideoURI(Uri.parse(value));
        vid.setMediaController(new MediaController(this));
        vid.start();

    }

}
