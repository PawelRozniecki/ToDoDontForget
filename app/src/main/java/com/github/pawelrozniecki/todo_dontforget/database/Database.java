package com.github.pawelrozniecki.todo_dontforget.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Database {


    public static final String DB_NAME = "com.github.pawelrozniecki.todo_dontforget.database.db";
    public static final int DB_VERSION = 18;

    public class DatabaseEntry implements BaseColumns {
        public static final String TABLE_NAME = "task_table";
        public static final String ID_COL = "ID";
        public static final String TASK_COL = "task_title";
        public static final String TASK_CONTENT_COL = "task_content";

        public static final String STATUS_COL = "status";
        public static final String DATE_COL = "date";
        public static final String CATEGORY_COL = "category";

    }

}
