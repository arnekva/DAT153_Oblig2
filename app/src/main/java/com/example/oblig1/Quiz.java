package com.example.oblig1;

import android.os.Bundle;

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
public class Quiz extends AppCompatActivity {

    private EditText sb_ans;
    private Button sb_submit;
    private TextView sb_score;
    private ImageView imgView;
    private String correctAnswer;
    private ArrayList<Image> quiz;
    private int score = 0;
    private int total = 0;
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
                if (actionId == EditorInfo.IME_ACTION_GO || (actionId == EditorInfo.IME_ACTION_DONE) || event.getAction() == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.ACTION_DOWN) {
                    //Perform your Actions here.
                    checkAnswer();
                }
                return handled;
            }
        });
    }

    /**
     * Checks whether the given answer is correct. Creates and shows a toast for either situation, and updates the scores
     */
    private void checkAnswer(){
        if (isCorrect(sb_ans.getText().toString(), correctAnswer)){
            score++;
            Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_SHORT).show();
            getNext();
        }else {
            Toast.makeText(getApplicationContext(), "Incorrect! \nThe correct answer is: " + correctAnswer, Toast.LENGTH_LONG).show();
        }
        updateScore();
    }

    /**
     * Updates the score and clears the input field. Increments total amount of tries.
     */
    private void updateScore(){
        total++;
        sb_score.setText("Score: " + score + "/" + total);
        sb_ans.getText().clear();
    }
    /**
     * Shuffles the list at the beginning of the quiz to get a new order each time
     */
    private void shuffleList(){
        quiz = ((GlobalStorage) this.getApplication()).getImages();
        Collections.shuffle(quiz);

        getNext();
    }
    private int counter = 0;

    /**
     * Gets the next image from the ArrayList. If the end of the list is reached, the Activity is finished and Toast is created with final score
     */
    private void getNext(){
        if (counter < quiz.size()) {
            imgView.setImageBitmap(quiz.get(counter).getBitmap());
            correctAnswer = quiz.get(counter).getName();
            counter++;
            //The code under will make the input field go back in focus
            sb_ans.post(new Runnable() {
                @Override
                public void run() {
                    sb_ans.requestFocus();
                }
            });
        }
        //This way we avoid having images repeated in a row.
        else if(counter == quiz.size()){
            counter = 0;
            shuffleList();
            }
    }
    private void exitAndToast(){
    finish();
    Toast.makeText(getApplicationContext(),"You finished with a score of " + score +" out of " + total,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitAndToast();
    }

    /**
     * Checks whether two strings are equal, not case sensitive
     * @param a String one
     * @param c String two
     * @return True if they match, false if not
     */
    private boolean isCorrect(String a, String c){
        return a.equalsIgnoreCase(c);
    }
}
