package com.example.user.laba4bobol;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class PictureViewerActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_viewer);
        imageView = (ImageView) findViewById(R.id.imageView2);
        Intent intent = getIntent();
        if(intent != null){
            String path = intent.getStringExtra("path");
            if(path != null){
                imageView.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }

    }
}
