package com.example.user.laba4bobol;

import java.io.File;

public class Keeper {
    static Keeper keeper;
    File fileSrc;
    File fileDsc;
    MyLinearLayout dscLayout;
    MyLinearLayout srcLayout;
    int action;
    private Keeper(){
        fileDsc = null;
        fileSrc = null;
        action = -1;
    }

    public static Keeper getInstance(){
        if(keeper == null)
            keeper = new Keeper();
        return keeper;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public File getFileSrc() {
        return fileSrc;
    }

    public void setFileSrc(File fileSrc) {
        this.fileSrc = fileSrc;
    }

    public File getFileDsc() {
        return fileDsc;
    }

    public void setFileDsc(File fileDsc) {
        this.fileDsc = fileDsc;
    }

    public MyLinearLayout getDscLayout() {
        return dscLayout;
    }

    public void setDscLayout(MyLinearLayout dscLayout) {
        this.dscLayout = dscLayout;
    }

    public MyLinearLayout getSrcLayout() {
        return srcLayout;
    }

    public void setSrcLayout(MyLinearLayout srcLayout) {
        this.srcLayout = srcLayout;
    }

    public boolean isFileSrcNull(){
        return (fileSrc == null);
    }

    public boolean isFileDscNull(){
        return (fileDsc == null);
    }
}
