package com.example.oblig1;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "image")
public class Image {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "imageId")
    private int id;

    @ColumnInfo(name = "name")
    private String name;
    private Bitmap bitmap;

    public Image(String n, Bitmap i){
        this.id = id;
        this.name = n;
        this.bitmap = i;
    }

    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
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
