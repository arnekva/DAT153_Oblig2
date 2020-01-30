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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.util.ArrayList;

public class Database extends AppCompatActivity {

    ListView list;
    ArrayList<Image> images;
    String[] name;
    Uri[] imageId;
    Image toBeUploaded;
    Button addToDb;
    Uri uploadImageUri;
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
    public void gotoAdd(View v){
        Intent intent = new Intent(this, AddImages.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        splitListToTable();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAdd(view);

            }
        });


        final CustomList adapter = new
                CustomList(Database.this, name, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

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
        list.invalidateViews();
        finish();
        startActivity(getIntent());
    }
}