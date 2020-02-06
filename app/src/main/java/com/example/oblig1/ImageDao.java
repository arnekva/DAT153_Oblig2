package com.example.oblig1;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface ImageDao {
    @Query("SELECT * FROM image")
    LiveData<ArrayList<Image>> getAllImages();

    @Insert
    void addImage(Image image);

    @Query("DELETE FROM image WHERE imageId = :id")
    void removeImage(int id);

    @Query("SELECT * FROM image WHERE imageId = :id")
    LiveData<Image> getImage(int id);

}
