package com.github.pawelrozniecki.todo_dontforget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.github.pawelrozniecki.todo_dontforget.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ManageListItem extends AppCompatActivity {
    private static final String TAG = "ManageListItem";
    DatabaseHelper databaseHelper;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    Button btn;
    EditText editText;
    View parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_list_item);

        databaseHelper = new DatabaseHelper(this);

        btn = findViewById(R.id.button2);
        editText = findViewById(R.id.editTask);
        fab = findViewById(R.id.fab);
        parent = findViewById(R.id.manage_layout);
        toolbar = findViewById(R.id.toolbar);


        //getting id and task title from the previous activity
        Intent receivedIntent = getIntent();
        final int receivedID = receivedIntent.getIntExtra("id", -1);
        final String receivedName = receivedIntent.getStringExtra("name");

        //set editText element to whatever is stored in the database for that taskID
        editText.setText(populateData(receivedName,receivedID));

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(receivedName);

        //click listeners
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String item = editText.getText().toString();
                databaseHelper.updateTaskContent(item, receivedID);
                Snackbar.make(parent, "Task saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("\n" + "\u2022");
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //loads the data from task_content column
    public String populateData(String title, int id){

        Cursor cursor = databaseHelper.getTaskContent(title,id);
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            Snackbar.make(parent, "Ops... Something went wrong", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{

            if(cursor.moveToFirst()){
                return cursor.getString(cursor.getColumnIndex("task_content"));

            }
            cursor.close();
        }

            return "error";
    }
}
