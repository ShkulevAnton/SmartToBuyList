package com.shkulant.smarttobuylist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddBuyProductActivity extends AppCompatActivity implements View.OnClickListener{

    EditText name;
    EditText amount;
    EditText measure;
    Button btn_confirm;
    Button btn_cancel;
    Button btn_delete;
    Spinner spinner;

    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        long list_id = intent.getLongExtra("list_id", -1);
        if(list_id == -2){
            this.finish();
        }
        setContentView(R.layout.add_buy_product);

        name = (EditText) findViewById(R.id.text_name_buy_add);
        amount = (EditText) findViewById(R.id.text_amount_buy_add);
        measure = (EditText) findViewById(R.id.text_measure_buy_add);
        btn_confirm = (Button) findViewById(R.id.btn_confirm_buy_add);
        btn_cancel = (Button) findViewById(R.id.btn_cancel_buy_add);
        btn_delete = (Button)findViewById(R.id.btn_delete_buy_add);
        spinner = (Spinner) findViewById(R.id.spinner_buy);

        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        long id = intent.getLongExtra("id", -1);


        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        Cursor c1 = db.query(DBHelper.TABLE_PRODUCTS,new String[]{DBHelper.KEY_PRODUCT},null,null,null,null,null);
        ArrayList<String> products = new ArrayList<String>();
        products.add("None");
        if (c1.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int titleColIndex = c1.getColumnIndex(dbhelper.KEY_PRODUCT);
            do {
                products.add(c1.getString(titleColIndex));
            } while (c1.moveToNext());
        }
        c1.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, products);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    dbhelper = new DBHelper(getApplicationContext());
                    SQLiteDatabase db = dbhelper.getReadableDatabase();
                    Cursor c1 = db.query(DBHelper.TABLE_PRODUCTS, null, null, null, null, null, null);
                    if (c1.moveToFirst()) {

                        // определяем номера столбцов по имени в выборке
                        int titleColIndex = c1.getColumnIndex(dbhelper.KEY_PRODUCT);
                        int amountColIndex = c1.getColumnIndex(dbhelper.KEY_DEFAMOUNT);
                        int measureColIndex = c1.getColumnIndex(dbhelper.KEY_MEASURE);
                        for (int i = 0; i < position-1; i++) {
                            c1.moveToNext();
                        }
                        name.setText(c1.getString(titleColIndex));
                        amount.setText(c1.getString(amountColIndex));
                        measure.setText(c1.getString(measureColIndex));
                    }
                    c1.close();
                    dbhelper.close();
                }else{
                    name.setText("");
                    amount.setText("");
                    measure.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(id == -1){
            spinner.setVisibility(View.VISIBLE);
            btn_confirm.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            amount.setVisibility(View.VISIBLE);
            measure.setVisibility(View.VISIBLE);
            btn_delete.setVisibility(View.GONE);

            spinner.setAdapter(adapter);

            name.setText("");
            amount.setText("");
            measure.setText("");
            btn_confirm.setText("Confirm");
        }
        else{
            spinner.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
            name.setVisibility(View.VISIBLE);
            amount.setVisibility(View.VISIBLE);
            measure.setVisibility(View.VISIBLE);
            Cursor c = db.query(DBHelper.TABLE_BUYLIST,null,DBHelper.KEY_TITLE + " = ?", new String[]{Long.toString(list_id+1)}, null, null, null);
            if (c.moveToFirst()) {
                for(int i = 0; i<id; i++){
                    c.moveToNext();
                }
                name.setText(c.getString(c.getColumnIndex(DBHelper.KEY_PRODUCT)));
                amount.setText(c.getString(c.getColumnIndex(DBHelper.KEY_AMOUNT)));
                measure.setText(c.getString(c.getColumnIndex(DBHelper.KEY_MEASURE)));
            }
            btn_delete.setVisibility(View.VISIBLE);
            btn_confirm.setText("Update");
        }


    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_cancel_buy_add){
            this.finish();
        }

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        long list_id = intent.getLongExtra("list_id", -1);

        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Cursor c = db.query(dbhelper.TABLE_BUYLIST, null, null, null, null, null, null);
        Cursor c1 = db.query(DBHelper.TABLE_BUYLIST,null,DBHelper.KEY_TITLE + " = ?", new String[]{Long.toString(list_id+1)}, null, null, null);

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_PRODUCT, name.getText().toString());
        cv.put(DBHelper.KEY_TITLE, list_id+1);
        cv.put(DBHelper.KEY_AMOUNT, amount.getText().toString());
        cv.put(DBHelper.KEY_MEASURE, measure.getText().toString());
        cv.put(DBHelper.KEY_CHECKED, 0);

        if(v.getId() == R.id.btn_confirm_buy_add) {
            if (id == -1) {
                cv.put(DBHelper.KEY_ID, c.getCount()+1);
                db.insert(DBHelper.TABLE_BUYLIST, null, cv);
            } else {
                if(c1.moveToFirst()) {
                    long need_id = 0;
                    while(need_id != id){
                        c1.moveToNext();
                        need_id++;
                    }
                    need_id = c1.getInt(c1.getColumnIndex(DBHelper.KEY_ID));
                    db.update(DBHelper.TABLE_BUYLIST, cv, DBHelper.KEY_ID + " = ?", new String[]{Long.toString(need_id)});
                }
            }
            this.finish();
        }

        if(v.getId() == R.id.btn_delete_buy_add) {
            if(c1.moveToFirst()) {
                long need_id = 0;
                while (need_id != id) {
                    c1.moveToNext();
                    need_id++;
                }
                need_id = c1.getInt(c1.getColumnIndex(DBHelper.KEY_ID));
                db.delete(DBHelper.TABLE_BUYLIST, DBHelper.KEY_ID + " = ?", new String[]{Long.toString(need_id)});
                db.execSQL("UPDATE " + DBHelper.TABLE_BUYLIST + " set " + DBHelper.KEY_ID + " = (" + DBHelper.KEY_ID + " - 1) WHERE " + DBHelper.KEY_ID + " > " + (need_id));
            }
            this.finish();
        }

        dbhelper.close();
    }
}
