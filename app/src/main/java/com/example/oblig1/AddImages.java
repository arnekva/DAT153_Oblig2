package com.example.oblig1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddImages extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Image toBeUploaded;
    Button addToDb;
    Uri uploadImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btn = (Button) findViewById(R.id.addButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRead();
            }
        });

        Button btn2 = (Button) findViewById(R.id.TakePhotoBtn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Toast.makeText(AddImages.this, "Nah", Toast.LENGTH_LONG).show();
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
        ImageView iw = findViewById(R.id.addImageViewer);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();

                uploadImageUri = Uri.parse(data.toUri(Intent.URI_ALLOW_UNSAFE));
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
        if (resultCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iw.setImageBitmap(imageBitmap);

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
