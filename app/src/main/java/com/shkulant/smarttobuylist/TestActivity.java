package com.shkulant.smarttobuylist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemLongClickListener {

    ListView listview;
    DBHelper dbhelper;
    ArrayAdapter<String> test_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, AddListActivity.class);
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

        listview = (ListView) findViewById(R.id.listview_test);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                if(selectedItem.equals("Not buy list")){
                    intent.putExtra("id", -2);
                }else{
                    intent.putExtra("id", id);
                }
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(this);

        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        //dbhelper.onUpgrade(db, 1,1);


        Cursor c = db.query(dbhelper.TABLE_LISTS, null, null, null, null, null, null);
        ArrayList<String> names = new ArrayList<String>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int titleColIndex = c.getColumnIndex(dbhelper.KEY_TITLE);
            do {
                names.add(c.getString(titleColIndex));
            } while (c.moveToNext());
        }
        names.add("Not buy list");
        c.close();

        test_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_text_test, names);
        listview.setAdapter(test_adapter);

        dbhelper.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_lists) {
            // Handle the camera action
        } else if (id == R.id.my_products) {
            Intent intent = new Intent(this, ProductsActivity.class);
            startActivity(intent);

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

        Cursor c = db.query(dbhelper.TABLE_LISTS, null, null, null, null, null, null);
        ArrayList<String> names = new ArrayList<String>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int titleColIndex = c.getColumnIndex(dbhelper.KEY_TITLE);
            do {
                names.add(c.getString(titleColIndex));
            } while (c.moveToNext());
        }
        names.add("Not buy list");
        c.close();

        test_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_text_test, names);
        listview.setAdapter(test_adapter);

        dbhelper.close();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(DBHelper.TABLE_LISTS,DBHelper.KEY_ID + " = ?", new String[]{Integer.toString(position+1)});
        db.execSQL("UPDATE "+DBHelper.TABLE_LISTS+" set " + DBHelper.KEY_ID + " = (" + DBHelper.KEY_ID + " - 1) WHERE " + DBHelper.KEY_ID + " > "+ (position+1));
        db.delete(DBHelper.TABLE_BUYLIST,DBHelper.KEY_TITLE + " = ?", new String[]{Integer.toString(position+1)});
        db.execSQL("UPDATE "+DBHelper.TABLE_BUYLIST+" set " + DBHelper.KEY_TITLE + " = (" + DBHelper.KEY_TITLE + " - 1) WHERE " + DBHelper.KEY_TITLE + " > "+ (position+1));
        Cursor c = db.query(dbhelper.TABLE_BUYLIST, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            for (int i = 1; i <= c.getCount(); i++) {
                db.execSQL("UPDATE " + DBHelper.TABLE_BUYLIST + " set " + DBHelper.KEY_ID + " = " + i + " WHERE " + DBHelper.KEY_ID + " = " + (c.getString(c.getColumnIndex(DBHelper.KEY_ID))));
                c.moveToNext();
            }
        }
        String selectedItem = parent.getItemAtPosition(position).toString();

        test_adapter.remove(selectedItem);
        test_adapter.notifyDataSetChanged();
        c.close();
        dbhelper.close();
        return true;
    }
}
