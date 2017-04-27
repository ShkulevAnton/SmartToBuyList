package com.shkulant.smarttobuylist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener{



    EditText name;
    EditText defamount;
    EditText measure;
    EditText storage;
    Button btn_confirm;
    Button btn_cancel;
    Button btn_delete;

    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        name = (EditText) findViewById(R.id.text_product_to_add);
        defamount = (EditText) findViewById(R.id.text_defamount_to_add);
        measure = (EditText) findViewById(R.id.text_measure_to_add);
        storage = (EditText) findViewById(R.id.text_storage_to_add);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_to_add);
        btn_cancel = (Button) findViewById(R.id.btn_cancel_to_add);
        btn_delete = (Button)findViewById(R.id.btn_delete_to_add);

        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);

        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        if(id == -1){
            btn_delete.setVisibility(View.GONE);
            name.setText("");
            defamount.setText("");
            measure.setText("");
            storage.setText("");
            btn_confirm.setText("Confirm");
        }
        else{
            Cursor c = db.query(DBHelper.TABLE_PRODUCTS,null,DBHelper.KEY_ID + " = ?", new String[]{Long.toString(id+1)}, null, null, null);
            if (c.moveToFirst()) {
                name.setText(c.getString(c.getColumnIndex(DBHelper.KEY_PRODUCT)));
                defamount.setText(c.getString(c.getColumnIndex(DBHelper.KEY_DEFAMOUNT)));
                measure.setText(c.getString(c.getColumnIndex(DBHelper.KEY_MEASURE)));
                storage.setText(c.getString(c.getColumnIndex(DBHelper.KEY_STORAGE)));
            }
            btn_delete.setVisibility(View.VISIBLE);
            btn_confirm.setText("Update");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_cancel_to_add){
            this.finish();
        }

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);

        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Cursor c = db.query(dbhelper.TABLE_PRODUCTS, null, null, null, null, null, null);

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_PRODUCT, name.getText().toString());
        cv.put(DBHelper.KEY_DEFAMOUNT, defamount.getText().toString());
        cv.put(DBHelper.KEY_MEASURE, measure.getText().toString());
        cv.put(DBHelper.KEY_STORAGE, storage.getText().toString());

        if(v.getId() == R.id.btn_confirm_to_add) {
            if (id == -1) {
                cv.put(DBHelper.KEY_ID, c.getCount()+1);
                db.insert(DBHelper.TABLE_PRODUCTS, null, cv);
            } else {
                db.update(DBHelper.TABLE_PRODUCTS, cv, DBHelper.KEY_ID + " = ?", new String[]{Long.toString(id + 1)});
            }
        }

        if(v.getId() == R.id.btn_delete_to_add) {
            db.delete(DBHelper.TABLE_PRODUCTS, DBHelper.KEY_ID + " = ?", new String[]{Long.toString(id + 1)});
            db.execSQL("UPDATE "+DBHelper.TABLE_PRODUCTS+" set " + DBHelper.KEY_ID + " = (" + DBHelper.KEY_ID + " - 1) WHERE " + DBHelper.KEY_ID + " > "+ (id+1));
        }

        dbhelper.close();
        this.finish();
    }
}
