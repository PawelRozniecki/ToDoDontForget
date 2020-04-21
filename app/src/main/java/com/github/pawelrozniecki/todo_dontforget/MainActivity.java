package com.github.pawelrozniecki.todo_dontforget;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import com.github.pawelrozniecki.todo_dontforget.database.Database;
import com.github.pawelrozniecki.todo_dontforget.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //Arrays holding all the task data temporarily
    private ArrayList <String> itemArray,dateArray, categoryArray;
    private RecyclerViewerAdapter adapter;
    private EditText editText;
//    private Button button;
    private DatePicker datePicker;
    private TextView list_item;
    private Toolbar toolbar;
    private DatabaseHelper helper;
    private RecyclerView recyclerView;
    View parent;
    ImageView imageView;
    Spinner spinner, dialogSpinner;
    StringBuilder sb;

    private String datePattern = "dd-MM-yyyy";
    private Dialog dialog;
    private FloatingActionButton fab;
    private SQLiteDatabase db;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dialog = new Dialog(this);
        helper = new DatabaseHelper(this);
        dialog.setContentView(R.layout.dialog_window);


        //finding views

        button = dialog.findViewById(R.id.button);
        datePicker = dialog.findViewById(R.id.datepicker);
        dialogSpinner = dialog.findViewById(R.id.dialog_spinner);
        editText = dialog.findViewById(R.id.enterText);
        fab = findViewById(R.id.fab);
        list_item = findViewById(R.id.list_item);
        parent = findViewById(R.id.parent_layout);
        recyclerView = findViewById(R.id.recycle_viewer);
        toolbar = findViewById(R.id.toolbar);


        datePicker.setMinDate(System.currentTimeMillis()-1000);
        //load the data from database
        populateData();

        //setting spinner contents
        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this,R.array.categories,
                android.R.layout.simple_spinner_item);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogSpinner.setAdapter(adapt);

        setSupportActionBar(toolbar);
        //hide title from toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //set title on action bar
        getSupportActionBar().setTitle("ToDo");

        //click listeners

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();


                String category = dialogSpinner.getSelectedItem().toString();
                //No boolean in SQLite , therefore 0 (false) and 1 (true) are used for checking status
                addNote(getDate(day,month,year),category, 0);
                populateData();

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.getText().clear();
                dialog.show();

            }
        });


    }

    private String getDate(int day, int month, int year){
        sb = new StringBuilder();
        sb.append(day);
        sb.append('/');
        sb.append(month);
        sb.append('/');
        sb.append(year);

        return sb.toString();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    private void filterCategories(String category){
//        itemArray = new ArrayList<>();
//        dateArray = new ArrayList<>();
//        Cursor data = helper.getCategoriesData(category);
//        while(data.moveToNext()){
//
//            itemArray.add(data.getString(1 ));
//            dateArray.add(data.getString(4));
//        }
//        adapter = new RecyclerViewerAdapter(itemArray, dateArray,this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
//
//    }
    private void populateData(){

        itemArray = new ArrayList<>();
        dateArray = new ArrayList<>();
        categoryArray = new ArrayList<>();
        imageView =  findViewById(R.id.icon);

        Cursor data = helper.getData();
        //iterate through all the rows
        while(data.moveToNext()){
            //adds results from column 1 (Task) to an array
            itemArray.add(data.getString(1 ));
            dateArray.add(data.getString(4));
            categoryArray.add(data.getString(3));

        }
        adapter = new RecyclerViewerAdapter(itemArray, dateArray,categoryArray,this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
    }
    private void addNote(String date, String category, int status){

        if(editText.getText().length()>0) {
            String note = editText.getText().toString();
            boolean insertTask = helper.addTask(note, date,category,status);
            if(insertTask ){

                Snackbar.make(parent, "Task added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else{
                Snackbar.make(parent, "Ops... Something went wrong", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }


        dialog.dismiss();



    }


    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                String name = itemArray.get(viewHolder.getAdapterPosition());
                Cursor data = helper.getDataID(name);

                int id = -1;
                while(data.moveToNext()){
                     id = data.getInt(0);
                }
                if(id>-1){

                    helper.deleteTask(id,name);
                    Snackbar.make(parent, "Task deleted", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(parent, "ERROR", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            itemArray.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
            populateData();





        }
    };
}
