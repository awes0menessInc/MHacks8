package org.octocats.lecturenotegenerator;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.content.ContentValues.TAG;


/**
 * Created by nisarg on 8/10/16.
 */
public class AddLecture extends Activity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    static final int REQUEST_TAKE_GALLERY_VIDEO = 2;

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
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);
                if (selectedImagePath != null) {

                    /*Intent intent = new Intent(HomeActivity.this,
                            VideoplayAvtivity.class);
                    intent.putExtra("path", selectedImagePath);
                    startActivity(intent);*/
                    File f = new File(""+data.getData());
                    String imageName = f.getName();
                    Log.e(TAG,imageName);


                    //UploadFile(selectedImageUri.toString());
                }
            } else if(requestCode == REQUEST_VIDEO_CAPTURE){
                String videoPath = data.getData().getPath();
                File f = new File(videoPath);
                String imageName = f.getName();
                Log.e(TAG,imageName);
            }
        }
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


    public void UploadFile(String fileName){
        try {
            // Set your file path here
            FileInputStream fstrm = new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/DCIM/"+fileName);

            // Set your server page url (and the file title/description)
            HttpFileUpload hfu = new HttpFileUpload("http://nisarg.me:3000/api/photo/", fileName);

            hfu.Send_Now(fstrm);

        } catch (FileNotFoundException e) {
            // Error: File not found
        }
    }
}
