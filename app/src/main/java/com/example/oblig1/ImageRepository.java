package com.example.oblig1;

import android.app.Application;

public class ImageRepository {
    private ImageDao imageDao;
    private ImageRoomDatabase db;

    public ImageRepository(Application application){
        db = ImageRoomDatabase.getDatabase(application);
        imageDao = db.imageDao();
    }

    public ImageDao getImageDao() {
        return imageDao;
    }

    public ImageRoomDatabase getDb() {
        return db;
    }
}
