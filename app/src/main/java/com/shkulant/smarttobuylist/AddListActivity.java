package com.shkulant.smarttobuylist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddListActivity extends AppCompatActivity implements View.OnClickListener{

    EditText text;
    Button btn;
    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_list);

        text = (EditText) findViewById(R.id.txt_add_list);
        btn = (Button) findViewById(R.id.btn_confim_add_list);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Cursor c = db.query(dbhelper.TABLE_LISTS, null, null, null, null, null, null);

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_ID, c.getCount()+1);
        cv.put(DBHelper.KEY_TITLE, text.getText().toString());

        db.insert(DBHelper.TABLE_LISTS, null, cv);

        dbhelper.close();
        this.finish();
    }
}
