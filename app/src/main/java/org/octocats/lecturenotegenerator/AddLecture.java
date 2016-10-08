package org.octocats.lecturenotegenerator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;


/**
 * Created by nisarg on 8/10/16.
 */
public class AddLecture extends Activity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_lec);

        ImageButton uploadBtn = (ImageButton) findViewById(R.id.upload);
        ImageButton recordBtn = (ImageButton) findViewById(R.id.record);

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
                    videoPath = takeVideoIntent.getData().getPath();
                }
            }


        });

    }
}
