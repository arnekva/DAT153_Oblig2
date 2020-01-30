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


public class AddImages extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_UPLOAD_IMAGE = 2;
    private Image toBeUploaded;
    private Button addToDb;
    private Bitmap uploadBitmap;
    private ImageView iw;

    @Override
    protected void onStart() {
        overridePendingTransition(0,0);
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_images);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iw = findViewById(R.id.addImageViewer);
        Button btn = findViewById(R.id.addButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRead();
            }
        });

        Button btn2 = findViewById(R.id.TakePhotoBtn);
        btn2.setOnClickListener(new View.OnClickListener() {
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
        btn = findViewById(R.id.addButton);
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
    public void goBackToDB(){
        Intent intent = new Intent(this, Database.class);
        intent.putExtra("flag", "return");
        startActivity(intent);
    }
    /**
     * Loads an image from the device gallery
     */
    public void loadUpImage(){
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

            //uploadImageUri = Uri.parse(data(Intent.URI_ALLOW_UNSAFE));
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
    public void addImageToDB(){
        EditText mEdit   = (EditText)findViewById(R.id.nameText);

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
    //TODO: CAMERA

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


}
