package com.example.mysqlitememo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deleteDatabase("nhs10627db");

        NHSDatabaseHelper helper = new NHSDatabaseHelper(this);
        EditText edit_title = findViewById(R.id.edit_title);
        EditText edit_text = findViewById(R.id.edit_text);

        Button button_search = findViewById(R.id.button_search);
        Button button_add = findViewById(R.id.button_add);
        Button button_clear = findViewById(R.id.button_clear);
        Button button_list = findViewById(R.id.button_list);
        Button button_delete = findViewById(R.id.button_delete);

        button_search.setOnClickListener(view -> {
            String[] cols = {"title", "memo", "write_date"};
            String[] params = {edit_title.getText().toString()};

            try (SQLiteDatabase database = helper.getWritableDatabase()) {
                Cursor cursor = database.query("memopad", cols, "title = ?", params, null, null, null, null);

                if (cursor.moveToFirst()) {
                    edit_text.setText(cursor.getString(1));
                    Toast.makeText(this, cursor.getString(0) + "の内容を読み込みました", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "指定されたタイトルはありません", Toast.LENGTH_LONG).show();
                }
            }
        });

        button_add.setOnClickListener(view -> {
            try(SQLiteDatabase database = helper.getWritableDatabase()) {
                Calendar calendar = Calendar.getInstance();
                Date date = new Date();
                calendar.setTime(date);

                int yy = calendar.get(Calendar.YEAR);
                int MM = calendar.get(Calendar.MONTH) + 1;
                int dd = calendar.get(Calendar.DATE);
                int hh = calendar.get(Calendar.HOUR);
                int mm = calendar.get(Calendar.MINUTE);
                int ss = calendar.get(Calendar.SECOND);
                String nowDate = yy + "/" + MM + "/" + dd + " " + hh + ":" + mm + ":" + ss;

                ContentValues contentValues = new ContentValues();
                contentValues.put("title", edit_title.getText().toString());
                contentValues.put("memo", edit_text.getText().toString());
                contentValues.put("write_date", nowDate);
                database.insert("memopad", null, contentValues);

                Toast.makeText(this, edit_title.getText().toString()+ "書き込みました", Toast.LENGTH_LONG).show();
            }
        });

        button_clear.setOnClickListener(view -> {
            edit_text.setText("");
        });

        button_list.setOnClickListener(view -> {
            String[] cols = {"title", "memo", "write_date"};
            try (SQLiteDatabase database = helper.getWritableDatabase()) {
                Cursor cursor = database.query("memopad", cols, null, null, null, null, null, null);
                boolean eol = cursor.moveToFirst();
                String res = "";

                while (eol){
                    res += cursor.getString(0) + "\n";
                    res += "　本文：" + cursor.getString(1) + "\n";
                    res += "　作成日付：" + cursor.getString(2) + "\n\n";
                    eol = cursor.moveToNext();
                }

                Log.d("DB", res);
                edit_text.setText(res);
                Toast.makeText(this, "全レコードを読み込みました", Toast.LENGTH_LONG).show();
            }
        });

        button_delete.setOnClickListener(view -> {
            try (SQLiteDatabase database = helper.getWritableDatabase()) {
                String[] params = {edit_title.getText().toString()};

                int i = database.delete("memopad", "title = ?", params);

                if (i == 1){
                    Toast.makeText(this, params[0] + "を削除しました", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "指定されたタイトルはありません", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}