package com.example.oblig1;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class GlobalStorage extends Application {

    private ArrayList<Image> images = new ArrayList<Image>();

    public ArrayList<Image> getImages(){
        return images;
    }
    public void addImage(Image image){
        images.add(image);
    }
    public boolean removeImage(Image image){
        return this.images.remove(image);
    }

    /**
     * Gets a specified image from the list
     * @param id The URI of the requested image
     * @return an image object
     */
    public Image getImage(Bitmap id){
        for(int i = 0;i<images.size();i++){
            if(images.get(i).getBitmap() == id){
                return images.get(i);
            }
        }
        return null;
    }

    /**
     * Remove all images from list
     */
    public void YeetAll(){
        images.removeAll(images);
    }
}
