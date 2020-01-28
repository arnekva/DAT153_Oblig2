package com.example.oblig1;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class TestAct extends AppCompatActivity {

    private EditText sb_ans;
    private Button sb_submit;
    private TextView sb_score;
    private ImageView imgView;
    private String correctAnswer;
    private ArrayList<Image> quiz;
    int score = 0;
    int total = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgView = findViewById(R.id.quizImageView);
        sb_ans = findViewById(R.id.submitText);
        sb_score = findViewById(R.id.tv_scoreText);
        sb_submit = findViewById(R.id.submitAnswer);

        initView();
        shuffleList();
    }

    private void initView(){
        sb_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
        sb_ans.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Perform your Actions here.
                    checkAnswer();
                }
                return handled;
            }
        });

    }
    public void checkAnswer(){
        if (isCorrect(sb_ans.getText().toString(), correctAnswer)){
            score++;
            total++;
            Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_SHORT).show();
            sb_score.setText(score + "/" + total);
            sb_ans.getText().clear();
            getNext();

        }else {
            total++;
            sb_score.setText(score + "/" + total);
            Toast.makeText(getApplicationContext(), "Incorrect! \nThe correct answer is: " + correctAnswer, Toast.LENGTH_LONG).show();
            sb_ans.getText().clear();
        }
    }
    public void shuffleList(){
        quiz = ((GlobalStorage) this.getApplication()).getImages();
        Collections.shuffle(quiz);
        getNext();
    }
    private int counter = 0;
    public void getNext(){
        if (counter < quiz.size()) {
            imgView.setImageURI(quiz.get(counter).getId());
            correctAnswer = quiz.get(counter).getName();
            counter++;
        }
        else if(counter == quiz.size()){
            finish();
            Toast.makeText(getApplicationContext(),"You finished with a score of " + score +" out of " + total,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean isCorrect(String a, String c){
        return a.trim().equalsIgnoreCase(c.trim());
    }

}
