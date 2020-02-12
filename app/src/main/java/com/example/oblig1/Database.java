package com.example.oblig1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.transition.Explode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class Database extends BaseActivity {

    private ListView list;
    private ArrayList<Image> images;
    private CustomList adapter;
    private ImageRepository repo;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onStart() {
        overridePendingTransition(0,0);
        super.onStart();
    }
    public void gotoAdd(View v){
        Intent intent = new Intent(this, AddImages.class);
        intent.putExtra("flag", "DB");
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        repo = new ImageRepository(getApplication());
        allImagesFromDB();
    }
    private void initVar(){
        adapter = new CustomList(Database.this, images);
        list=findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    /**
     * Adds event listeners to buttons in the activity
     */
    public void makeEventListeners(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAdd(view);

            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Database.this, "You Clicked at " +images.get(position).getName(), Toast.LENGTH_SHORT).show();

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
                builder.setMessage("Are you sure you want to delete " + images.get(pos).getName()+ " from the database?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Image image = images.get(pos);
                                deleteFromDB(image);
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

    private void allImagesFromDB(){

        class GetImage extends AsyncTask<Void, Void, List<Image>>{

            @Override
            protected List<Image> doInBackground(Void... voids) {
                return repo.getImageDao().getAllImages();
            }

            @Override
            protected void onPostExecute(List<Image> imageList) {
                super.onPostExecute(imageList);
                images = (ArrayList<Image>) imageList;
                initVar();
                makeEventListeners();
            }
        }
        GetImage gi = new GetImage();
        gi.execute();
    }
    private void deleteFromDB(final Image toBeRemoved){

        class DeleteImage extends AsyncTask<Void, Void, Integer>{

            @Override
            protected Integer doInBackground(Void... voids) {
                return repo.getImageDao().removeImage(toBeRemoved.getImageId());
            }

            @Override
            protected void onPostExecute(Integer numDelete) {
                super.onPostExecute(numDelete);
                if(numDelete>0){
                    adapter.remove(toBeRemoved);
                    Toast.makeText(Database.this, "You deleted " + toBeRemoved.getName() +" from the database", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Database.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
        DeleteImage di = new DeleteImage();
        di.execute();
    }
}