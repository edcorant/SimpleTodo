package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // to-do list container
    List<String> items;
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

        items = new ArrayList<>();

        ItemsAdapter my_todo_items = new ItemsAdapter(items);
        todo_list.setAdapter(my_todo_items);
        todo_list.setLayoutManager(new LinearLayoutManager(this));
    }
}