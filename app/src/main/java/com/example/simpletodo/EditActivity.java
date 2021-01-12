package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getCanonicalName();

    EditText update_todo;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        update_todo = findViewById(R.id.todo_item);
        save = findViewById(R.id.save);

        getSupportActionBar().setTitle(R.string.edit_item);

        update_todo.setText(getIntent().getStringExtra(getString(R.string.KEY_ITEM_TEXT)));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra(getString(R.string.KEY_ITEM_TEXT), update_todo.getText().toString());
                i.putExtra(getString(R.string.KEY_ITEM_POSITION), getIntent().getExtras().getInt(getString(R.string.KEY_ITEM_POSITION)));
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}