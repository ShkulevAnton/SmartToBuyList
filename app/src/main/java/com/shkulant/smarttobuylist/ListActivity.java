package com.shkulant.smarttobuylist;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity{

    ListView listview;
    DBHelper dbhelper;
    long list_id;
   // Context con = getApplicationContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddBuyProductActivity.class);
                intent.putExtra("id", -1);
                intent.putExtra("list_id", list_id);
                startActivity(intent);
            }
        });


        listview = (ListView) findViewById(R.id.listview_buylist);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddBuyProductActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("list_id", list_id);
                startActivity(intent);
            }
        });



        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        list_id = id;
        Cursor c;
        if(id != -1) {
             c = db.query(dbhelper.TABLE_BUYLIST, new String[]{dbhelper.KEY_ID + " AS _id", dbhelper.KEY_PRODUCT, dbhelper.KEY_AMOUNT, dbhelper.KEY_MEASURE, dbhelper.KEY_CHECKED, dbhelper.KEY_ID}, dbhelper.KEY_TITLE + " = ?", new String[]{Long.toString(id + 1)}, null, null, null);
        }else{
            c = db.query(dbhelper.TABLE_BUYLIST, new String[]{dbhelper.KEY_ID + " AS _id", dbhelper.KEY_PRODUCT, dbhelper.KEY_AMOUNT, dbhelper.KEY_MEASURE, dbhelper.KEY_CHECKED, dbhelper.KEY_ID}, dbhelper.KEY_CHECKED + " = 0", null, null, null, null);
        }
        //MyAdapter ad = new MyAdapter(this, c);
        ArrayList<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int titleColIndex = c.getColumnIndex(dbhelper.KEY_PRODUCT);
            int defamountColIndex = c.getColumnIndex(dbhelper.KEY_AMOUNT);
            int measureColIndex = c.getColumnIndex(dbhelper.KEY_MEASURE);
            int checkedColIndex = c.getColumnIndex(dbhelper.KEY_CHECKED);
            int idColIndex = c.getColumnIndex(dbhelper.KEY_ID);
            do {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(DBHelper.KEY_PRODUCT, c.getString(titleColIndex));
                m.put(DBHelper.KEY_AMOUNT, c.getString(defamountColIndex));
                m.put(DBHelper.KEY_MEASURE, c.getString(measureColIndex));
                m.put(DBHelper.KEY_CHECKED, c.getString(checkedColIndex));
                m.put(DBHelper.KEY_ID, c.getString(idColIndex));
                products.add(m);
            } while (c.moveToNext());
        }
        c.close();

        String[] from ={dbhelper.KEY_PRODUCT, dbhelper.KEY_AMOUNT, dbhelper.KEY_MEASURE, dbhelper.KEY_CHECKED, dbhelper.KEY_ID};
        int[] to = {R.id.text_product_buy, R.id.text_amount_buy, R.id.text_measure_buy, R.id.checkBox_buy, R.id.text_id_buy};

        MyAdapter product_adapter = new MyAdapter(getApplicationContext(), products, from, to);
        listview.setAdapter(product_adapter);

        dbhelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        dbhelper = new DBHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", -1);
        list_id = id;

        Cursor c;
        if(id != -1) {
            c = db.query(dbhelper.TABLE_BUYLIST, new String[]{dbhelper.KEY_ID + " AS _id", dbhelper.KEY_PRODUCT, dbhelper.KEY_AMOUNT, dbhelper.KEY_MEASURE, dbhelper.KEY_CHECKED, dbhelper.KEY_ID}, dbhelper.KEY_TITLE + " = ?", new String[]{Long.toString(id + 1)}, null, null, null);
        }else{
            c = db.query(dbhelper.TABLE_BUYLIST, new String[]{dbhelper.KEY_ID + " AS _id", dbhelper.KEY_PRODUCT, dbhelper.KEY_AMOUNT, dbhelper.KEY_MEASURE, dbhelper.KEY_CHECKED, dbhelper.KEY_ID}, dbhelper.KEY_CHECKED + " = 0", null, null, null, null);
        }
        ArrayList<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int titleColIndex = c.getColumnIndex(dbhelper.KEY_PRODUCT);
            int defamountColIndex = c.getColumnIndex(dbhelper.KEY_AMOUNT);
            int measureColIndex = c.getColumnIndex(dbhelper.KEY_MEASURE);
            int checkedColIndex = c.getColumnIndex(dbhelper.KEY_CHECKED);
            int idColIndex = c.getColumnIndex(dbhelper.KEY_ID);
            do {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(DBHelper.KEY_PRODUCT, c.getString(titleColIndex));
                m.put(DBHelper.KEY_AMOUNT, c.getString(defamountColIndex));
                m.put(DBHelper.KEY_MEASURE, c.getString(measureColIndex));
                m.put(DBHelper.KEY_CHECKED, c.getString(checkedColIndex));
                m.put(DBHelper.KEY_ID, c.getString(idColIndex));
                products.add(m);
            } while (c.moveToNext());
        }
        c.close();

        String[] from = {dbhelper.KEY_PRODUCT, dbhelper.KEY_AMOUNT, dbhelper.KEY_MEASURE, dbhelper.KEY_CHECKED, dbhelper.KEY_ID};
        int[] to = {R.id.text_product_buy, R.id.text_amount_buy, R.id.text_measure_buy, R.id.checkBox_buy, R.id.text_id_buy};

        MyAdapter product_adapter = new MyAdapter(getApplicationContext(), products, from, to);
        listview.setAdapter(product_adapter);

        dbhelper.close();
    }

    private class ViewHolder {
        TextView name;
        TextView amount;
        TextView measure;
        CheckBox check;
        TextView id;
    }

    private static void setUpAlarm(final Context context, final Intent intent, final int timeInterval)
    {
        final AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pi = PendingIntent.getBroadcast(context, timeInterval, intent, 0);
        am.cancel(pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            final AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + timeInterval, pi);
            am.setAlarmClock(alarmClockInfo, pi);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInterval, pi);
        else
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeInterval, pi);
    }

    private class MyAdapter extends SimpleAdapter {


        SQLiteDatabase db;
        LayoutInflater inflater;
        Context context;
        ArrayList<Map<String, Object>> arrayList;

        public MyAdapter(Context context, ArrayList<Map<String, Object>> cur, String[]from, int[]to) {
            super(context,cur, R.layout.listview_buylist, from, to);
            this.context = context;
            this.arrayList = cur;
            inflater.from(context);
            dbhelper = new DBHelper(context);
            db = dbhelper.getWritableDatabase();
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            final String up_id;
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listview_buylist, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.text_product_buy);
                holder.amount = (TextView) convertView.findViewById(R.id.text_amount_buy);
                holder.measure = (TextView) convertView.findViewById(R.id.text_measure_buy);
                holder.check = (CheckBox) convertView.findViewById(R.id.checkBox_buy);
                holder.id = (TextView) convertView.findViewById(R.id.text_id_buy);
                //up_id = holder.id.getText().toString();
                convertView.setTag(holder);
                holder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dbhelper = new DBHelper(context);
                        db = dbhelper.getWritableDatabase();
                        CheckBox v1 = (CheckBox)v;
                        ContentValues cv = new ContentValues();
                        if (v1.isChecked()) {
                            cv.put(DBHelper.KEY_CHECKED, 1);
                            db.update(DBHelper.TABLE_BUYLIST, cv, DBHelper.KEY_ID + " = ?", new String[]{v1.getText().toString()});
                            android.support.v4.app.NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(context)
                                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                                            .setContentTitle("My notification")
                                            .setContentText("Hello World!")
                                            .setAutoCancel(true);
                            int mNotificationId = 001;
// Gets an instance of the NotificationManager service
                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());
                        }
                        else{
                            cv.put(DBHelper.KEY_CHECKED, 0);
                            db.update(DBHelper.TABLE_BUYLIST, cv, DBHelper.KEY_ID + " = ?", new String[]{v1.getText().toString()});
                        }
                        dbhelper.close();
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, Object> product = arrayList.get(position);
            holder.name.setText((product.get(DBHelper.KEY_PRODUCT)).toString());
            holder.amount.setText((product.get(DBHelper.KEY_AMOUNT)).toString());
            holder.measure.setText((product.get(DBHelper.KEY_MEASURE)).toString());
            holder.id.setText((product.get(DBHelper.KEY_ID)).toString());
            holder.check.setText((product.get(DBHelper.KEY_ID)).toString());
            int i = Integer.valueOf((String)(product.get(DBHelper.KEY_CHECKED)));
            if( i == 0){
                holder.check.setChecked(false);
            } else {
                holder.check.setChecked(true);
            }


            return convertView;


        }
    }
}
