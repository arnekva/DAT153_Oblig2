package com.example.oblig1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.ByteArrayOutputStream;

@Entity(tableName = "image")
public class Image {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "imageId")
    private int imageId;

    @ColumnInfo(name = "name")
    private String name;

    private byte[] encodedImage;

    public Image(){}

    public Image(String name, Bitmap bitmap) {
        this.name = name;
        setEncodedImage(bitmap);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        // byte[] byteArray = Base64.decode(this.encodedImage, Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(this.encodedImage, 0 ,this.encodedImage.length);
        return bm;
    }

    public byte[] getEncodedImage(){
        return encodedImage;
    }

    public void setEncodedImage(byte[] encodedImage){
        this.encodedImage = encodedImage;
    }

    public void setEncodedImage(Bitmap bitmap) {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, boas ); //bm is the bitmap object
        encodedImage = boas.toByteArray();
//        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
//        this.encodedImage = encodedImage;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(@NonNull int imageId) {
        this.imageId = imageId;
    }
}