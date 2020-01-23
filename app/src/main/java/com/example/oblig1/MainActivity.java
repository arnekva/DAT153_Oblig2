package com.example.oblig1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.provider.Settings;
import android.view.View;

import android.os.Bundle;
import android.widget.EditText;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

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
        // Do something in response to button
        Intent intent = new Intent(this, TestAct.class);

        startActivity(intent);
    }
    public void gotoDB(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Database.class);

        startActivity(intent);
    }

    private void initImages(){
        Image img1 = new Image("Amy", R.drawable.amy);
        Image img2 = new Image("Jake", R.drawable.jake);
        Image img3 = new Image("Hitchcock", R.drawable.hitchcock);
        Image img4 = new Image("Boyle", R.drawable.boyle);
        ((GlobalStorage) this.getApplication()).addImage(img1);
        ((GlobalStorage) this.getApplication()).addImage(img2);
        ((GlobalStorage) this.getApplication()).addImage(img3);
        ((GlobalStorage) this.getApplication()).addImage(img4);

    }
}
