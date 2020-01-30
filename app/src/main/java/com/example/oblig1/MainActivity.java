package com.example.oblig1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImages();
    }

    private void startQuiz(View view) {
        Intent intent = new Intent(this, Quiz.class);
        startActivity(intent);
    }
    private void gotoDB(View view) {
        Intent intent = new Intent(this, Database.class);
        startActivity(intent);
    }
    private void gotoAdd(View v){
        Intent intent = new Intent(this, AddImages.class);
        startActivity(intent);
    }

    private void initImages(){
        ((GlobalStorage) this.getApplication()).YeetAll();
        Bitmap bm_amy = BitmapFactory.decodeResource(getResources(), R.drawable.amy);
        Bitmap bm_jake = BitmapFactory.decodeResource(getResources(), R.drawable.jake);
        Bitmap bm_hitchcock = BitmapFactory.decodeResource(getResources(), R.drawable.hitchcock);
        Bitmap bm_boyle = BitmapFactory.decodeResource(getResources(), R.drawable.boyle);
        Image img1 = new Image("Amy", bm_amy);
        Image img2 = new Image("Jake", bm_jake);
        Image img3 = new Image("Hitchcock", bm_hitchcock);
        Image img4 = new Image("Boyle", bm_boyle);
        ((GlobalStorage) this.getApplication()).addImage(img1);
        ((GlobalStorage) this.getApplication()).addImage(img2);
        ((GlobalStorage) this.getApplication()).addImage(img3);
        ((GlobalStorage) this.getApplication()).addImage(img4);
    }
}
