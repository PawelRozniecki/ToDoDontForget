package com.github.pawelrozniecki.todo_dontforget.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, Database.DB_NAME, null, Database.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //SQL QUERY FOR CREATING DATA ENTRY TABLE IN DB

        String CREATE = "CREATE TABLE " + Database.DatabaseEntry.TABLE_NAME +
                "(" + Database.DatabaseEntry.ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Database.DatabaseEntry.TASK_COL +
                " TEXT NOT NULL, " + Database.DatabaseEntry.TASK_CONTENT_COL + " TEXT, "+ Database.DatabaseEntry.CATEGORY_COL +
                " TEXT NOT NULL, " + Database.DatabaseEntry.DATE_COL + " TEXT, "+ Database.DatabaseEntry.STATUS_COL + " INTEGER  "+
        ");";
        db.execSQL(CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Database.DatabaseEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //Creates a new task
    public boolean addTask(String item, String date, String category, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.DatabaseEntry.TASK_COL, item);
        contentValues.put(Database.DatabaseEntry.CATEGORY_COL, category);
        contentValues.put(Database.DatabaseEntry.DATE_COL, date);
        contentValues.put(Database.DatabaseEntry.STATUS_COL, status);

        Log.d(TAG, "addTask: Adding " + item + " to " + Database.DatabaseEntry.TABLE_NAME);
        long result = db.insert(Database.DatabaseEntry.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    //fetches all the rows from the table
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Database.DatabaseEntry.TABLE_NAME, null);
    }

    public Cursor getCategoriesData(String category){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + Database.DatabaseEntry.TABLE_NAME + " WHERE " + Database.DatabaseEntry.CATEGORY_COL  + " ='" +
                category + "'",null);

    }

    //fetches rowID for the given task

    public Cursor getDataID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT " + Database.DatabaseEntry.ID_COL + " FROM " + Database.DatabaseEntry.TABLE_NAME +
                " WHERE " + Database.DatabaseEntry.TASK_COL + "='" + name + "'", null);

    }


    //Fetches task content based on the name and id of the task
    public Cursor getTaskContent(String name, int id ){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.rawQuery("SELECT " + Database.DatabaseEntry.TASK_CONTENT_COL + " FROM " + Database.DatabaseEntry.TABLE_NAME+
                " WHERE " + Database.DatabaseEntry.ID_COL + "='"+ id + "'" +
                " AND " + Database.DatabaseEntry.TASK_COL + "='" + name + "'",null);
    }

    //Sets the content based on user input in the edit text

    public void updateTaskContent(String name, int id ){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql =   "UPDATE " + Database.DatabaseEntry.TABLE_NAME + " SET " + Database.DatabaseEntry.TASK_CONTENT_COL +
                "='" + name +"'" + " WHERE " + Database.DatabaseEntry.ID_COL + "='"+ id + "'";
        db.execSQL(sql);
    }


    //Deletes the given task. Function is called in MainActivity in onSwipe() method

    public void deleteTask(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + Database.DatabaseEntry.TABLE_NAME + " WHERE " + Database.DatabaseEntry.ID_COL + "='" + id + "'" + " AND "
                + Database.DatabaseEntry.TASK_COL + "= '" + name + "'";

        db.execSQL(sql);

    }
}