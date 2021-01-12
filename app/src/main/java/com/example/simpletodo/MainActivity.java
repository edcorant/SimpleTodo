package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getCanonicalName();

    // to-do list container
    List<String> items;

    ItemsAdapter my_todo_items;

    // UI elements
    Button add;
    EditText text_box;
    RecyclerView todo_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // link UI elements to class objects
        add = findViewById(R.id.add);
        text_box = findViewById(R.id.text_box);
        todo_list = findViewById(R.id.todo_list);

        // populate list with to-do's from app file
        loadItems();

        ItemsAdapter.OnLongClickListener my_listener = new ItemsAdapter.OnLongClickListener() {

            @Override
            public void onItemLongClicked(int position) {
                String removed_item = items.get(position);
                items.remove(position);
                my_todo_items.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), removed_item + " removed from the list!", Toast.LENGTH_SHORT).show();

                // save changes to file
                saveItems();
            }
        };

        my_todo_items = new ItemsAdapter(items, my_listener);
        todo_list.setAdapter(my_todo_items);
        todo_list.setLayoutManager(new LinearLayoutManager(this));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get string value from text box
                String item = text_box.getText().toString();
                // add to array list
                items.add(item);
                my_todo_items.notifyItemInserted(items.size() - 1);
                // erase text box contents
                text_box.setText("");

                Toast.makeText(getApplicationContext(), item + " added to the list!", Toast.LENGTH_SHORT).show();

                // save changes to app file
                saveItems();
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e) {
            Log.e(TAG, "Error reading from file", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        }
        catch (IOException e) {
            Log.e(TAG, "Error writing to file", e);
        }
    }
}