package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    private final int EDIT_TEXT_CODE = 100;

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
        ItemsAdapter.OnClickListener my_lisneter = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                // create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass the data being edited
                i.putExtra(getString(R.string.KEY_ITEM_TEXT), items.get(position));
                i.putExtra(getString(R.string.KEY_ITEM_POSITION), position);
                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        my_todo_items = new ItemsAdapter(items, my_listener, my_lisneter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            String item_text = data.getStringExtra(getString(R.string.KEY_ITEM_TEXT));
            int position = data.getExtras().getInt(getString(R.string.KEY_ITEM_POSITION));
            items.set(position, item_text);
            my_todo_items.notifyItemChanged(position);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item at position " + position + " is now " + item_text, Toast.LENGTH_SHORT).show();
        }

        else {
            Log.w(TAG, "Uknown call to onActivityResult()");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}