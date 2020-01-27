package com.example.oblig1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

public class Database extends AppCompatActivity {

    ListView list;
    ArrayList<Image> images;
    String[] name;
    Uri[] imageId;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private void splitListToTable(){
        images = ((GlobalStorage) this.getApplication()).getImages();
        name = new String[images.size()];
        imageId = new Uri[images.size()];
        for (int i = 0; i < images.size(); i++){
            name[i] = images.get(i).getName();
            imageId[i] = images.get(i).getId();
        }
    }

    @Override
    protected void onStart() {
        overridePendingTransition(0,0);
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        splitListToTable();
        final CustomList adapter = new
                CustomList(Database.this, name, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        Button btn = (Button) findViewById(R.id.addButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRead();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Database.this, "You Clicked at " +name[+ position], Toast.LENGTH_SHORT).show();

            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final int pos = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(Database.this);
                builder.setCancelable(true);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure you want to delete?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Image image = ((GlobalStorage) getApplication()).getImage(imageId[pos]);
                                updateViewHack();
                                ((GlobalStorage) getApplication()).removeImage(image);

                                Toast.makeText(Database.this, "You deleted " +name[+ pos] +" from the database", Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Database.this, "Nothing was deleted", Toast.LENGTH_SHORT).show();

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();






                return true;
            }

        });




    }
    public void updateViewHack(){
        list.requestLayout();
        finish();
        startActivity(getIntent());
    }
    //For API 23 og opp må man be om tilgang
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
                Uri uri = Uri.parse(data.toUri(Intent.URI_ALLOW_UNSAFE));
                System.out.println();
                //final InputStream imageStream = getContentResolver().openInputStream(imageUri);
               // final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                EditText mEdit   = (EditText)findViewById(R.id.nameText);
                String name = mEdit.getText().toString();
                Image img = new Image(name, uri);
                System.out.println("URI: " + uri);
               ((GlobalStorage) getApplication()).addImage(img);
                updateViewHack();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Database.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(Database.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }




}