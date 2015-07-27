package com.jaellysbales.nowfeed;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by c4q-rosmary on 6/30/15.
 */
public class TodoCard {

    private static View todoView;
    private final static String STORETEXT="storetext.txt";

    private static EditText txtEditor;

    public TodoCard (View todoView){
        this.todoView = todoView;
    }

    public static View createTodoView (View fragmentView ){
        todoView = fragmentView.findViewById(R.id.todoCardView);


        txtEditor=(EditText) todoView.findViewById(R.id.textBox_editText);

        Button saveBtn = (Button) todoView.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(saveListener);

        readFileInEditor();

        return todoView;
    }

    public static View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            try {
                OutputStreamWriter out= new OutputStreamWriter(todoView.getContext().openFileOutput(STORETEXT, 0));
                out.write(txtEditor.getText().toString());
                out.close();
                Toast.makeText(todoView.getContext(), "The contents are saved in the file.", Toast.LENGTH_LONG).show();
            }
            catch (Throwable t) {
                Toast.makeText(todoView.getContext(), "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
            }

        }
    };

    public static void readFileInEditor () {
        try {
            InputStream in = todoView.getContext().openFileInput(STORETEXT);
            if (in != null){
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str;
                StringBuilder buf = new StringBuilder();
                while ((str = reader.readLine()) != null){
                    buf.append(str+"n");
                }
                in.close();
                txtEditor.setText(buf.toString());
            }
        }catch (java.io.FileNotFoundException e) {
            //haven't created yet
        }catch(Throwable t){
            Toast.makeText(todoView.getContext(), "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
