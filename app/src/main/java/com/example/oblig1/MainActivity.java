package com.example.oblig1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.InputType;
import android.view.View;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    private String m_Text = "";
    private ImageRepository repo;
    private boolean dataBaseIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo = new ImageRepository(getApplication());
        dataBaseIsEmpty = false;

        checkDBSize();
        checkPreferences();
    }

    @Override
    protected void onStart() {
        overridePendingTransition(0,0);
        super.onStart();
    }
    public void startQuiz(View view) {
        if(dataBaseIsEmpty){
            Toast.makeText(getApplicationContext(), "The database is empty. Try adding some images", Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(this, Quiz.class);
            startActivity(intent);
        }
     }

    public void gotoDB(View view) {
        if(dataBaseIsEmpty){
            Toast.makeText(getApplicationContext(), "The database is empty. Try adding some images", Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(this, Database.class);
            startActivity(intent);
        }
    }
    public void gotoAdd(View v){
        Intent intent = new Intent(this, AddImages.class);
        startActivity(intent);
    }



    private void checkPreferences(){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        final Editor editor = pref.edit();
        editor.remove("owner");
        editor.commit();
        String owner = pref.getString("owner", null);
        if(owner == null || owner.equals("")){
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

        builder.setTitle("Owner");
        builder.setMessage("Please enter your name");
        final EditText input = new EditText(this);
        input.setId(999);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                if(m_Text.equals("")){
                    m_Text = "rip";
                }
                editor.putString("owner", m_Text);
                editor.commit();
            }
        });
        builder.show();
    }
    private void checkDBSize(){

        class GetImage extends AsyncTask<Void, Void, Image> {

            @Override
            protected Image doInBackground(Void... voids) {
                    return repo.getImageDao().getRandomImage(-1);
            }

            @Override
            protected void onPostExecute(Image image) {
                super.onPostExecute(image);
                if(image == null){
                    dataBaseIsEmpty = true;
                }
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }
}
