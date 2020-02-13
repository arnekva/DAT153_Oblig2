package com.example.oblig1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class Settings extends AppCompatActivity {
    TextView tv_owner;
    EditText edit_field;
    Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        tv_owner = findViewById(R.id.currentOwner);
        edit_field = findViewById(R.id.editOwner);
        save_button = findViewById(R.id.saveButton);
        String owner = getOwner();
        tv_owner.setText(owner);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewOwner();
            }
        });
    }

    private void saveNewOwner(){
        String input = edit_field.getText().toString();
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        final SharedPreferences.Editor editor = pref.edit();
        if(input != null && !input.trim().equals("")){
            editor.putString("owner", input);
            editor.commit();
            Toast.makeText(Settings.this, "New owner is " + input, Toast.LENGTH_LONG).show();
            String display_name = capitalizeWord(input);
            tv_owner.setText(display_name);
        }else{
            Toast.makeText(Settings.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    private String getOwner(){
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("sharedPreferences", 0);
        return pref.getString("owner", null);
    }

    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }
}
