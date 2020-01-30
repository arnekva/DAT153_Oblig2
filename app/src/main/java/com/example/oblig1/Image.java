package com.example.oblig1;

import android.graphics.Bitmap;

public class Image {
    private String name;
    private Bitmap bitmap;

    public Image(String n, Bitmap i){
        this.name = n;
        this.bitmap = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap id) {
        this.bitmap = id;
    }
}
