package com.example.user.laba4bobol;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class AudioPlayerActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    ProgressBar progressBar;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    Thread threadTime;
    Button button;
    String path;
    boolean threadState;
    java.text.DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        dateFormat = DateFormat.getTimeFormat(this);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        button = (Button) findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        threadState = true;
        Intent intent = getIntent();
        if(intent != null){
            String path = intent.getStringExtra("path");
            String name = intent.getStringExtra("name");
            if(path != null){
                this.path = path;
                if(name != null){
                    textView3.setText(name);
                }
            }else{
                finish();
            }
        }else{
            finish();
        }
    }

    public void onClickButton(View view) {
        try{
            if(mediaPlayer != null){
                if(!mediaPlayer.isPlaying()){
                    button.setText(R.string.pause);
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    progressBar.setMax(mediaPlayer.getDuration());
                    textView5.setText(format(mediaPlayer.getDuration()));
                    mediaPlayer.start();
                    threadState = true;
                    final Handler handler = new Handler();
                    threadTime = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                while(threadState){
                                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                                    handler.post(new Runnable() {
                                        public void run() {
                                            textView4.setText(
                                                    format(mediaPlayer.getCurrentPosition()));
                                        }
                                    });
                                    Thread.sleep(500);
                                }
                            }catch (Exception e){}
                        }
                    });
                    threadTime.start();
                }else{
                    mediaPlayer.pause();
                    threadState = false;
                    button.setText(R.string.play);
                }
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    String format(long milliseconds){
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);
        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        String format = "%d:";
        if(hours != 0)
            format = "%d:" + format;
        if(seconds < 10){
            format += "0%d";
        }else{
            format += "%d";
        }
        if(hours == 0){
            return String.format(format, minutes, seconds);
        }else{
            return String.format(format, hours, minutes, seconds);
        }
    }

    public void onButton4Click(View view) {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            threadState = false;
            button.setText(R.string.play);
            progressBar.setProgress(0);
            textView4.setText(format(0));
        }
    }

    public void onButton3Click(View view) {
        if(mediaPlayer != null){
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 3000);
        }
    }

    public void onButton2Click(View view) {
        if(mediaPlayer != null){
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 3000);
        }
    }
}
