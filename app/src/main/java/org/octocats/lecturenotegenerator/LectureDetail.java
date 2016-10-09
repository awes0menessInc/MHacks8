package org.octocats.lecturenotegenerator;

import android.app.Activity;
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

        String value;
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            value = extras.get("key").toString();
        }
        else
            value = null;

        VideoView vid = (VideoView) findViewById(R.id.videoView);
        vid.setVideoPath(value);
        vid.setMediaController(new MediaController(this));
        vid.start();

    }

}
