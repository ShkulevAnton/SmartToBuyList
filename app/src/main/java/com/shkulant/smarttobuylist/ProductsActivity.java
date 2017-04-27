package com.shkulant.smarttobuylist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listview;
    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                intent.putExtra("id", -1);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listview = (ListView) findViewById(R.id.listview_products);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Cursor c = db.query(dbhelper.TABLE_PRODUCTS, null, null, null, null, null, null);
        ArrayList<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int titleColIndex = c.getColumnIndex(dbhelper.KEY_PRODUCT);
            int defamountColIndex = c.getColumnIndex(dbhelper.KEY_DEFAMOUNT);
            int measureColIndex = c.getColumnIndex(dbhelper.KEY_MEASURE);
            int storageColIndex = c.getColumnIndex(dbhelper.KEY_STORAGE);
            do {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(DBHelper.KEY_PRODUCT, c.getString(titleColIndex));
                m.put(DBHelper.KEY_DEFAMOUNT, c.getString(defamountColIndex));
                m.put(DBHelper.KEY_MEASURE, c.getString(measureColIndex));
                m.put(DBHelper.KEY_STORAGE, c.getString(storageColIndex));
                products.add(m);
            } while (c.moveToNext());
        }
        c.close();

        String[] from ={dbhelper.KEY_PRODUCT, dbhelper.KEY_DEFAMOUNT, dbhelper.KEY_MEASURE, dbhelper.KEY_STORAGE};
        int[] to = {R.id.text_product, R.id.text_defamount, R.id.text_measure, R.id.text_storage};

        SimpleAdapter product_adapter = new SimpleAdapter(this, products, R.layout.listview_text_products, from, to);
        listview.setAdapter(product_adapter);

        dbhelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_lists) {
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_products) {

        } else if (id == R.id.my_exit) {
            finishAffinity();
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Cursor c = db.query(dbhelper.TABLE_PRODUCTS, null, null, null, null, null, null);
        ArrayList<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int titleColIndex = c.getColumnIndex(dbhelper.KEY_PRODUCT);
            int defamountColIndex = c.getColumnIndex(dbhelper.KEY_DEFAMOUNT);
            int measureColIndex = c.getColumnIndex(dbhelper.KEY_MEASURE);
            int storageColIndex = c.getColumnIndex(dbhelper.KEY_STORAGE);
            do {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(DBHelper.KEY_PRODUCT, c.getString(titleColIndex));
                m.put(DBHelper.KEY_DEFAMOUNT, c.getString(defamountColIndex));
                m.put(DBHelper.KEY_MEASURE, c.getString(measureColIndex));
                m.put(DBHelper.KEY_STORAGE, c.getString(storageColIndex));
                products.add(m);
            } while (c.moveToNext());
        }
        c.close();

        String[] from ={dbhelper.KEY_PRODUCT, dbhelper.KEY_DEFAMOUNT, dbhelper.KEY_MEASURE, dbhelper.KEY_STORAGE};
        int[] to = {R.id.text_product, R.id.text_defamount, R.id.text_measure, R.id.text_storage};

        SimpleAdapter product_adapter = new SimpleAdapter(this, products, R.layout.listview_text_products, from, to);
        listview.setAdapter(product_adapter);

        dbhelper.close();
    }
}
