package com.example.oblig1;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface ImageDao {

    @Query("SELECT * FROM image")
    List<Image> getAllImages();

    @Insert
    void addImage(Image image);

    @Query("DELETE FROM image WHERE imageId = :id")
    int removeImage(int id);

    @Query("SELECT * FROM image WHERE imageId NOT LIKE :id ORDER BY RANDOM() LIMIT 1;")
    Image getRandomImage(int id);

    @Query("DELETE FROM image")
    void nukeTable();

}
