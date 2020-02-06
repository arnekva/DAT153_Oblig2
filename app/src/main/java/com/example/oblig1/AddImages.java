package com.example.oblig1;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AddImages extends BaseActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_UPLOAD_IMAGE = 2;
    private Image toBeUploaded;
    private Button addToDb;
    private Bitmap uploadBitmap;
    private ImageView iw;
    EditText mEdit;

    @Override
    protected void onStart() {
        overridePendingTransition(0,0);
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        iw = findViewById(R.id.addImageViewer);
        Button uploadBtn = findViewById(R.id.addButton);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRead();
            }
        });

        Button cameraBtn = findViewById(R.id.TakePhotoBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dispatchTakePictureIntent();
            }
        });
        FloatingActionButton fab = findViewById(R.id.fabadd);
        Intent intent = getIntent();
        String checkFlag= intent.getStringExtra("flag");

        if(!"DB".equals(checkFlag)){
            fab.hide();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToDB();
            }
        });

        addToDb = findViewById(R.id.addBtn);
        addToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToDB();
            }
        });

    }

    /**
     * Requests access to gallery.
     * On newer versions of Android, the app must request access to gallery from the system. If granted, it will load selected image
     */
    private void requestRead() {
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
    private void goBackToDB(){
        Intent intent = new Intent(this, Database.class);
        intent.putExtra("flag", "return");
        startActivity(intent);
    }
    /**
     * Loads an image from the device gallery
     */
    private void loadUpImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_UPLOAD_IMAGE);
    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            iw.setImageBitmap(imageBitmap);
            uploadBitmap = imageBitmap;
        }
    if(resultCode == RESULT_OK && reqCode == REQUEST_UPLOAD_IMAGE){
        try {
            final Uri imageUri = data.getData();
            Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            iw.setImageBitmap(bitmapImg);
            uploadBitmap = bitmapImg;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(AddImages.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }
    }
    /**
     * Adds an image to the app database.
     * Checks whether the image file or name text is empty before adding.
     */
    private void addImageToDB(){
        mEdit = findViewById(R.id.nameText);
        if(uploadBitmap!= null && !mEdit.getText().toString().trim().isEmpty()) {
            String name = mEdit.getText().toString();
            toBeUploaded = new Image(name, uploadBitmap);
            ((GlobalStorage) getApplication()).addImage(toBeUploaded);
            toBeUploaded = null;
            finish();
            Intent intentdb = new Intent(this, Database.class);
            startActivity(intentdb);
            Toast.makeText(AddImages.this, "Image added to database", Toast.LENGTH_LONG).show();
        }else{
            if(mEdit.getText().toString().trim().isEmpty()){
                Toast.makeText(AddImages.this, "You need to enter the name of the person", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(AddImages.this, "You need to upload an image first", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
