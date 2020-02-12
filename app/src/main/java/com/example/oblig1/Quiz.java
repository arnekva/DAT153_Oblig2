package com.example.oblig1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz extends BaseActivity {

    private EditText sb_ans;
    private Button sb_submit;
    private TextView sb_score;
    private ImageView imgView;
    private Image currentImage;
    private int score = 0;
    private int total = 0;
    private ImageRepository repo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initVar();
        initView();
        imageFromDb();
    }

    /**
     * Initializes the variables
     */
    private void initVar(){
        imgView = findViewById(R.id.quizImageView);
        sb_ans = findViewById(R.id.submitText);
        sb_score = findViewById(R.id.tv_scoreText);
        sb_submit = findViewById(R.id.submitAnswer);
        repo = new ImageRepository(getApplication());
    }

    /**
     * Initializes the event listeners
     */
    private void initView(){
        sb_ans.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO || (actionId == EditorInfo.IME_ACTION_DONE) || event.getAction() == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.ACTION_DOWN) {
                    //Perform your Actions here.
                    checkAnswer(null);
                }
                return handled;
            }
        });
    }

    /**
     * Checks whether the given answer is correct. Creates and shows a toast for either situation, and updates the scores
     */
    public void checkAnswer(View view){
        if (sb_ans.getText().toString().equalsIgnoreCase(currentImage.getName())){
            score++;
            Toast.makeText(getApplicationContext(),"Correct!",Toast.LENGTH_SHORT).show();
            imageFromDb();
        }else {
            Toast.makeText(getApplicationContext(), "Incorrect! \nThe correct answer is: " + currentImage.getName(), Toast.LENGTH_LONG).show();
        }
        updateScore();
        imageFromDb();
    }
    @Override
    public void onStop(){
        super.onStop();
        exitAndToast();
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
     * Shows the score as a toast when the user exits the quiz
     */
    private void exitAndToast(){
        finish();
        Toast.makeText(getApplicationContext(),"You finished with a score of " + score +" out of " + total,Toast.LENGTH_SHORT).show();

    }

    /**
     * Gets the next image from the database. The same image cannot appear in a row
     */
    private void imageFromDb(){

        class GetImage extends AsyncTask<Void, Void, Image> {

            @Override
            protected Image doInBackground(Void... voids) {
                if(currentImage != null){
                    Image image1 = repo.getImageDao().getRandomImage(currentImage.getImageId());
                    return image1 == null ? repo.getImageDao().getRandomImage(-1) : image1;
                }
                return repo.getImageDao().getRandomImage(-1);
            }
            @Override
            protected void onPostExecute(Image image) {
                super.onPostExecute(image);

                imgView.setImageBitmap(image.getBitmap());
                currentImage = image;
            }
        }
            GetImage gi = new GetImage();
            gi.execute();
        }
}