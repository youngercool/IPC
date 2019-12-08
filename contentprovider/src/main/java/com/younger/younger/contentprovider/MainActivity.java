package com.younger.younger.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri bookUri = Uri.parse("content://com.younger.younger.contentprovider.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id",6);
        values.put("name","程序设计语言");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri,
                new String[]{"_id","name"},null,null,null);

        while (bookCursor.moveToNext()){
            Log.e("====","===bookId="+bookCursor.getInt(0)+" ,===bookName="+bookCursor.getString(1));
        }
        bookCursor.close();
    }
}
