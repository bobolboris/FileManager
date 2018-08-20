package com.example.user.laba4bobol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.laba4bobol.until.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyLinearLayout extends LinearLayout implements
        View.OnClickListener, View.OnLongClickListener {
    Activity context;
    View caption;
    List<MyLinearLayout> items;
    MyLinearLayout parentLayout;
    boolean state;
    boolean open;
    int nesting;
    File path;
    Keeper keeper;
    LayoutInflater inflater;
    LinearLayout.LayoutParams ll;

    public boolean isOpen(){
        return state;
    }

    public boolean isFile(){
        return path.isFile();
    }

    public File getPath() {
        return path;
    }

    public void clear(){
        if(items != null)
            items.clear();
    }

    public MyLinearLayout getParentLayout() {
        return parentLayout;
    }

    public void setParentLayout(MyLinearLayout parentLayout) {
        this.parentLayout = parentLayout;
    }

    public void add(MyLinearLayout myLinearLayout){
        myLinearLayout.setLayoutParams(ll);
        myLinearLayout.setVisibility(View.GONE);
        items.add(myLinearLayout);
        addView(myLinearLayout);
    }

    public void openTree(){
        if(open){
            for(MyLinearLayout mm: items) {
                mm.openTree();
            }
        }else{
            showAll();
        }
    }

    public boolean closeTree(){
        //1, true - мы закрылись, 0, false - и был закрыт
        List<Boolean> list = new ArrayList<>();
        for(MyLinearLayout mm: items) {
            list.add(mm.closeTree());
        }
        for(Boolean b: list)
            if(b) return true;
        if(open){
            hideAll();
            return true;
        }else
            return false;
    }

    public void createThis(File directory){
        if(directory.isDirectory()){
            File[] files = directory.listFiles();
            if(files == null) return;
            clear();
            for(File file: files){
                MyLinearLayout ml = new MyLinearLayout(context, file,
                        this, nesting + 1);
                add(ml);
            }
        }
    }

    void init(){
        //this.setDuplicateParentStateEnabled(false);
        open = false;
        state = false;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        items = new ArrayList<>();
        caption = inflater.inflate(R.layout.itemlayout, this);
        String tm1p = path.getName();
        ((TextView) caption.findViewById(R.id.textView)).setText((tm1p.length() > 15)?
                                                                        tm1p.substring(0, 14):
                                                                                        tm1p);
        caption.findViewById(R.id.textView2).setVisibility((path.listFiles() == null)?
                                                                                    View.INVISIBLE:
                                                                                    View.VISIBLE);
        ((ImageView) caption.findViewById(R.id.imageView)).setImageResource((path.isDirectory())?
                                                                            R.drawable.folder:
                                                                            R.drawable.file);
        caption.setOnClickListener(this);
        ll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        ll.setMargins(10 * (nesting + 1), 0, 0, 0);
        this.setOnLongClickListener(this);
        keeper = Keeper.getInstance();
    }

    public MyLinearLayout(Activity context, File path, MyLinearLayout parentLayout, int nesting) {
        super(context);
        this.path = path;
        this.parentLayout = parentLayout;
        this.setOrientation(LinearLayout.VERTICAL);
        this.context = context;
        this.nesting = nesting;
        init();
    }

    public void hideAll(){
        caption.findViewById(R.id.textView2).setVisibility((path.listFiles() == null)?
                                                                                    View.INVISIBLE:
                                                                                    View.VISIBLE);
        for(MyLinearLayout mm: items) {
            mm.setVisibility(View.GONE);
        }
        open = false;
    }

    public void showAll(){
        caption.findViewById(R.id.textView2).setVisibility(View.INVISIBLE);
        items.clear();
        createThis(path);
        for(MyLinearLayout mm: items) {
            mm.setVisibility(View.VISIBLE);
        }
        open = true;
    }

    @Override
    public void onClick(View v) {
        if(v == caption){
            if(!state){
                if(path.isFile()){
                    openFile(path);
                }else {
                    state = true;
                    showAll();
                }
                //caption.findViewById(R.id.relativeLayout).setBackgroundColor(Color.BLUE);
            }else{
                state = false;
                hideAll();
                //caption.findViewById(R.id.relativeLayout).setBackgroundColor(Color.WHITE);
            }

        }
    }

    void openFile(File file){
        switch (getTypeFile(file)){
            case ".mp3": case ".mp2": case ".wav": case ".ogg":{
                Intent intent = new Intent(context, AudioPlayerActivity.class);
                intent.putExtra("name", file.getName());
                intent.putExtra("path", file.getAbsolutePath());
                context.startActivity(intent);
                break;
            }

            case ".jpg": case ".jpeg": case ".png": case ".bmp": case ".gif":{
                Intent intent = new Intent(context, PictureViewerActivity.class);
                intent.putExtra("path", file.getAbsolutePath());
                context.startActivity(intent);
                break;
            }

            case ".avi": case ".mpg": case ".mp4": case ".3gp":{
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("path", file.getAbsolutePath());
                context.startActivity(intent);
                break;
            }

            default:{
                Toast.makeText(context,
                        context.getString(R.string.errorFileType), Toast.LENGTH_LONG).show();
                break;
            }
        }

    }

    String getTypeFile(File file) {
        String fileName = file.getName();
        int index = -1;
        int lastIndex = -1;
        do {
            index = fileName.indexOf('.', index + 1);
            if (index != -1)
                lastIndex = index;
        } while (index != -1);
        if (lastIndex == -1 || lastIndex == fileName.length() - 1)
            return null;
        return fileName.substring(lastIndex, fileName.length());
    }

    public void update(){
        if(isOpen()){
            hideAll();
            createThis(getPath());
            showAll();
        }
    }

    public void copy(){
        keeper.setAction(1);
    }

    public void cut(){
        keeper.setAction(2);
    }

    public void delete(){
        if(!keeper.isFileSrcNull()){
            Files.delete(keeper.getFileSrc());
            MyLinearLayout mm = keeper.getSrcLayout().getParentLayout();
            if(mm != null) mm.update();
            keeper.setFileSrc(null);
            keeper.setSrcLayout(null);
        }
    }

    public void paste(){
        try{
            if(!keeper.isFileSrcNull() && !keeper.isFileDscNull()){
                if(keeper.getAction() == 1){
                    Files.copyFileOrDirectory(keeper.getFileSrc(), keeper.getFileDsc());
                }else if(keeper.getAction() == 2){
                    Files.copyFileOrDirectory(keeper.getFileSrc(), keeper.getFileDsc());
                    Files.delete(keeper.getFileSrc());
                    MyLinearLayout mm = keeper.getSrcLayout().getParentLayout();
                    if(mm != null) mm.update();
                }
                keeper.getDscLayout().update();
                keeper.setAction(-1);
                keeper.setFileSrc(null);
                keeper.setFileDsc(null);
                keeper.setSrcLayout(null);
                keeper.setDscLayout(null);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onLongClick(View v) {
        if(keeper.isFileSrcNull() || isFile()){
            keeper.setFileSrc(path);
            keeper.setSrcLayout(this);
            context.showDialog(0);
        }else{
            keeper.setFileDsc(path);
            keeper.setDscLayout(this);
            context.showDialog(1);
        }
        return false;
    }

}
