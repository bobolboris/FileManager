package com.example.user.laba4bobol;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {
    VideoView videoView;
    MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mediaController = new MediaController(this);
        videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setMediaController(mediaController);
        Intent intent = getIntent();
        if(intent != null){
            String path = intent.getStringExtra("path");
            if(path !=  null){
                videoView.setVideoPath(path);
                videoView.start();
            }else{
                finish();
            }

        }else{
            finish();
        }

    }
}
