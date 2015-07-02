package com.jaellysbales.nowfeed.db;

import android.provider.BaseColumns;

/**
 * Created by c4q-rosmary on 7/2/15.
 */
public class TaskContract {

    public static final String DB_NAME = "com.jaellysbales.nowfeed.db.tasks";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
    }
}
