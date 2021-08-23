package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText updateText;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        updateText = findViewById(R.id.updateText);
        btnSave = findViewById(R.id.buttonSave);

        getSupportActionBar().setTitle("Edit Item");
        updateText.setText(getIntent().getStringExtra(MainActivity.TEXT_KEY));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditActivity.this, MainActivity.class);
                i.putExtra(MainActivity.TEXT_KEY, updateText.getText().toString());
                int position = getIntent().getIntExtra(MainActivity.POSITION_KEY,0);
                i.putExtra(MainActivity.POSITION_KEY, position);

                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}