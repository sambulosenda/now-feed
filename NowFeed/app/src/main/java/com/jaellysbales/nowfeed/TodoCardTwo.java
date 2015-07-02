package com.jaellysbales.nowfeed;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.jaellysbales.nowfeed.db.TaskContract;
import com.jaellysbales.nowfeed.db.TaskDBHelper;

/**
 * Created by c4q-rosmary on 7/2/15.
 */
public class TodoCardTwo {

    protected static TaskDBHelper helper;
    private static ListAdapter listAdapter;
    private static ImageButton addItemBtn;
    private static ListView listView;

    public static View todoViewTwo;

    public TodoCardTwo (View todoViewTwo){
        this.todoViewTwo = todoViewTwo;
    }

    public static View createTodoTwoView (LayoutInflater inflater){
        todoViewTwo = inflater.inflate(R.layout.todo_card_two, null);

        listView = (ListView) todoViewTwo.findViewById(android.R.id.list);
        listView.setOnTouchListener(listViewListener);

        updateUI();
        addItemBtn = (ImageButton) todoViewTwo.findViewById(R.id.add_item_btn);
        addItemBtn.setOnClickListener(addItemlistener);

        return todoViewTwo;
    }

    protected static void updateUI() {
        helper = new TaskDBHelper(todoViewTwo.getContext());
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);

        listAdapter = new SimpleCursorAdapter(
                todoViewTwo.getContext(),
                R.layout.todo_task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK},
                new int[]{R.id.taskTextView},
                0
        );

        //this.setListAdapter(listAdapter);
        listView.setAdapter(listAdapter);
    }

    public static View.OnClickListener addItemlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder builder = new AlertDialog.Builder(todoViewTwo.getContext());
            builder.setTitle("Add a task");
            builder.setMessage("What do you want to do?");
            final EditText inputField = new EditText(todoViewTwo.getContext());
            builder.setView(inputField);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("MainActivity", inputField.getText().toString());

                    String task = inputField.getText().toString();
                    Log.d("MainActivity", task);

                    TaskDBHelper helper = new TaskDBHelper(todoViewTwo.getContext());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.clear();
                    values.put(TaskContract.Columns.TASK, task);

                    db.insertWithOnConflict(TaskContract.TABLE, null, values,
                            SQLiteDatabase.CONFLICT_IGNORE);

                    updateUI();

                }
            });

            builder.setNegativeButton("Cancel", null);

            builder.create().show();

        }
    };


    public static View.OnTouchListener listViewListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            // Handle ListView touch events.
            v.onTouchEvent(event);
            return true;

        }
    };


}
