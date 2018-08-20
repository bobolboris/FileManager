package com.example.user.laba4bobol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    float fx;
    float fy;
    float width;
    MyLinearLayout my;
    AlertDialog.Builder builder;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        my = new MyLinearLayout(this,
                new File(Environment.getExternalStorageDirectory().toString()),
                null, 0);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.mLayout);
        linearLayout.addView(my);
        width = 0.8f * getWindowManager().getDefaultDisplay().getWidth();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        final String[] items;
        if(id == 0){
            items = new String[]{getString(R.string.copy), getString(R.string.cut),
                    getString(R.string.delete)};
        }else{
            items = new String[]{getString(R.string.copy), getString(R.string.cut),
                    getString(R.string.delete), getString(R.string.paste)};
        }
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.selectAction));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item){
                    case 0:
                        my.copy();
                        break;
                    case 1:
                        my.cut();
                        break;
                    case 2:
                        my.delete();
                        break;
                    case 3:
                        my.paste();
                        break;
                }
            }
        });
        builder.setCancelable(true);
        return builder.create();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                fx = x;
                fy = y;
                break;
            case MotionEvent.ACTION_UP: // отпускание
                float tmp = x - fx;
                if(Math.abs(tmp) >= width){
                    if(tmp > 0){
                        my.closeTree();
                    }else{
                        my.openTree();
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public void onMenu0Click(MenuItem item) {
        startActivity(new Intent(this, CameraActivity.class));
    }

    public void onMenu1Click(MenuItem item) {
        startActivity(new Intent(this, RecordAudioActivity.class));
    }
}