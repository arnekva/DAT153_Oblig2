package com.example.oblig1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddImages extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    Image toBeUploaded;
    Button addToDb;
    Uri uploadImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btn = (Button) findViewById(R.id.addButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRead();
            }
        });
        addToDb = findViewById(R.id.addBtn);
        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToDB();
            }
        });
        btn = (Button) findViewById(R.id.addButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRead();
            }
        });
    }
    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            loadUpImage();
        }
    }
    public void loadUpImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        startActivityForResult(photoPickerIntent, 1);
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();

                uploadImageUri = Uri.parse(data.toUri(Intent.URI_ALLOW_UNSAFE));
                ImageView iw = findViewById(R.id.addImageViewer);
                iw.setImageURI(uploadImageUri);
                //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                // final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(AddImages.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(AddImages.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    public void addImageToDB(){
        EditText mEdit   = (EditText)findViewById(R.id.nameText);

        if(uploadImageUri!= null && !mEdit.getText().toString().trim().isEmpty()) {

            String name = mEdit.getText().toString();
            System.out.println("X"+name+"X");
            toBeUploaded = new Image(name, uploadImageUri);
            System.out.println("URI: " + uploadImageUri);
            ((GlobalStorage) getApplication()).addImage(toBeUploaded);
            toBeUploaded = null;
            finish(); //TODO: not finish?
            Toast.makeText(AddImages.this, "Image added to database", Toast.LENGTH_LONG).show();
        }else{
            if(mEdit.getText().toString().trim().isEmpty()){
                Toast.makeText(AddImages.this, "You need to enter the name of the person", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(AddImages.this, "You need to upload an image first", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateViewHack(){
        finish();
        startActivity(getIntent());
    }

}
