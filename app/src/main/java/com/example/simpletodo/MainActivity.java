package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    public static final String TEXT_KEY = "text_to_be_updated";
    public static final String POSITION_KEY = "position_of_update";
    public static final int TEXT_CODE = 2021;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        readFile();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Item was removed", Toast.LENGTH_SHORT).show();
                writeFile();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra(TEXT_KEY, items.get(position));
                i.putExtra(POSITION_KEY, position);
                startActivityForResult(i, TEXT_CODE);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemText = etItem.getText().toString();
                items.add(itemText);
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                Toast.makeText(MainActivity.this, "Item was added", Toast.LENGTH_SHORT).show();
                writeFile();
            }
        });
    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    private void readFile(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    private void writeFile(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == TEXT_CODE) {
            String updateText = data.getStringExtra(TEXT_KEY);
            int position = data.getIntExtra(POSITION_KEY,0);

            items.set(position, updateText);
            itemsAdapter.notifyItemChanged(position);
            writeFile();
            Toast.makeText(this, "Item has been updated", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.v("MainActivity", "Unknown call to onActvityyResult");
        }
    }
}