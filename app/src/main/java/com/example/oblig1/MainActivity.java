package com.example.oblig1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.view.View;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImages();
        checkPreferences();
    }
    @Override
    protected void onStart() {
        overridePendingTransition(0,0);
        super.onStart();
    }
    public void startQuiz(View view) {
        Intent intent = new Intent(this, Quiz.class);
        if(!((GlobalStorage) this.getApplication()).getImages().isEmpty()){
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this, "The database us empty. Try to add some images before you start the quiz", Toast.LENGTH_SHORT).show();
        }
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

    private void checkPreferences(){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        final Editor editor = pref.edit();
        String owner = pref.getString("owner", null);
        if(owner == null){
            inputName(editor);
        }else{
            String checkFlag= getIntent().getStringExtra("flag");
            if(!"homebtn".equals(checkFlag)){
                Toast.makeText(getApplicationContext(), "Welcome, " + Settings.capitalizeWord(owner), Toast.LENGTH_LONG).show();
            }

        }
    }
    public void inputName(Editor e){
        final Editor editor = e;
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);

        builder.setTitle("Enter name");
        builder.setMessage("Please enter your name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                editor.putString("owner", m_Text);
                editor.commit();
            }
        });
        builder.show();
    }
}
