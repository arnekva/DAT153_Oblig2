package com.example.oblig1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImages();


    }


    public MainActivity() {
    }

    public void startQuiz(View view) {
        Intent intent = new Intent(this, Quiz.class);
        startActivity(intent);
    }
    public void gotoDB(View view) {
        Intent intent = new Intent(this, Database.class);
        startActivity(intent);
    }
    public void gotoAdd(View v){
        Intent intent = new Intent(this, AddImages.class);
        startActivity(intent);

    }

    private void initImages(){
        Image img1 = new Image("Amy", Uri.parse("android.resource://com.example.oblig1/"+R.drawable.amy));
        Image img2 = new Image("Jake", Uri.parse("android.resource://com.example.oblig1/"+R.drawable.jake));
        Image img3 = new Image("Hitchcock", Uri.parse("android.resource://com.example.oblig1/"+R.drawable.hitchcock));
        Image img4 = new Image("Boyle", Uri.parse("android.resource://com.example.oblig1/"+R.drawable.boyle));
        ((GlobalStorage) this.getApplication()).addImage(img1);
        ((GlobalStorage) this.getApplication()).addImage(img2);
        ((GlobalStorage) this.getApplication()).addImage(img3);
        ((GlobalStorage) this.getApplication()).addImage(img4);

        for(int i = 0;i<((GlobalStorage) this.getApplication()).getImages().size();i++){
            System.out.println(((GlobalStorage) this.getApplication()).getImages().get(i));
        }

    }
}
