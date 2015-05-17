package com.example.thai.myapplication.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thai.myapplication.R;
import com.example.thai.myapplication.adapter.DiaryListAdapter;
import com.example.thai.myapplication.database.DatabaseHelper;
import com.example.thai.myapplication.model.DiaryItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DiaryListActivity extends ActionBarActivity {
    private static final String EXTRA_ID_DIARY = "extra_diary_id";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private ArrayList<DiaryItem> diaryList;
//    private ListView lvDiary;
    private RecyclerView mRecyclerView;
    private DiaryListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);
        setupToolbar();

//        lvDiary = (ListView) findViewById(R.id.lvDiary);
        diaryList = DatabaseHelper.getInstance(this).getAllDiary();
        String[] data = new String[20];
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(1000000) + "";
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.lvDiary);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DiaryListAdapter(data);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("My Diary");
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();


//        if (!diaryList.isEmpty()) {
//            ArrayList<String> values = new ArrayList<String>();
//            for (DiaryItem diary : diaryList) {
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(diary.createTime);
//                String strDate = simpleDateFormat.format(cal.getTime());
//
//                values.add(strDate);
//            }
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
//            lvDiary.setAdapter(adapter);
//
//            lvDiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    DiaryItem diary = diaryList.get(position);
//                    Intent intent = new Intent(DiaryListActivity.this, ComposeActivity.class);
//                    intent.putExtra(EXTRA_ID_DIARY, id);
//                    startActivity(intent);
//
//                }
//            });
//        } else {
//            Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diary_list, menu);
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
            Intent intent = new Intent(DiaryListActivity.this, ComposeActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
