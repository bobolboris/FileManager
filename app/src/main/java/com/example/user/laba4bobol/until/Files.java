package com.example.user.laba4bobol.until;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Files {

    public static String readAllText(String path) throws IOException {
        return readAllText(new File(path));
    }

    private static String readAllText(File file) throws IOException {
        StringBuilder text = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        br.close();
        return text.toString();
    }


    public static byte[] readBytesFromFile(String path) {
        return readBytesFromFile(new File(path));
    }

    private static byte[] readBytesFromFile(File file) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;
        try {
            bytesArray = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return bytesArray;
    }

    public static void delete(String path){
        delete(new File(path));
    }

    public static void delete(File file){
        file.delete();
    }

    public static void copyFileOrDirectory(File src, File dst) throws IOException {
        String path = dst.getAbsolutePath() + "/" + src.getName();
        if (src.isDirectory()) {
            File f = new File(path);
            f.mkdir();
            for (String file : src.list()) {
                copyFileOrDirectory(new File(src, file), f);
            }
        } else {
            copyFile(src, new File(path));
        }
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

}
