package com.example.user.laba4bobol;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class CameraActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    Camera camera;
    MediaRecorder mediaRecorder;
    File pictures;
    Time time;
    Button button6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        time = new Time();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        pictures = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "Camera");
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        button6 = (Button) findViewById(R.id.button6);

        SurfaceHolder holder = surfaceView.getHolder();
        final Context context = this;
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera.setPreviewDisplay(holder);
                    camera.startPreview();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override public void surfaceChanged(SurfaceHolder h, int f, int w, int he) {}
            @Override public void surfaceDestroyed(SurfaceHolder holder) {}
        });
    }

    private boolean prepareVideoRecorder() {
        camera.unlock();

        mediaRecorder = new MediaRecorder();
        time.setToNow();
        File videoFile = new File(pictures,
                Long.toString(time.toMillis(false)) + ".3gp");

        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setProfile(CamcorderProfile
                .get(CamcorderProfile.QUALITY_HIGH));
        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = Camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        if (camera != null)
            camera.release();
        camera = null;
    }

    public void onButton6Click(View view) {
        button6.setVisibility(View.INVISIBLE);
        if (prepareVideoRecorder()) {
            mediaRecorder.start();
        } else {
            releaseMediaRecorder();
        }
    }

    public void onButton7Click(View view) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            releaseMediaRecorder();
        }
        button6.setVisibility(View.VISIBLE);
    }

    public void onClick5Button(View view) {
        time.setToNow();
        final File photoFile = new File(pictures,
                Long.toString(time.toMillis(false)) + ".jpg");
        final Context context = this;
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    FileOutputStream fos = new FileOutputStream(photoFile);
                    fos.write(data);
                    fos.close();
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}



