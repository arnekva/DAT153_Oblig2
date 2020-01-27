package com.example.oblig1;

import android.net.Uri;

public class Image {
    private String name;
    private Uri id;

    public Image(String n, Uri i){
        this.name = n;
        this.id = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getId() {
        return id;
    }

    public void setId(Uri id) {
        this.id = id;
    }
}
