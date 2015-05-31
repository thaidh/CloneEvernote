package com.example.thai.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.example.thai.myapplication.R;
import com.example.thai.myapplication.adapter.DiaryListAdapter;
import com.example.thai.myapplication.database.DatabaseHelper;
import com.example.thai.myapplication.model.DiaryItem;

import java.util.ArrayList;

public class DiaryListActivity extends ActionBarActivity {
    public static final String EXTRA_DIARY_CONTENT = "extra_diary_content";

    private ArrayList<DiaryItem> diaryList = new ArrayList<DiaryItem>();
    private RecyclerView mRecyclerView;
    private DiaryListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AQuery mAQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);
        setupToolbar();

        mAQ = new AQuery(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.lvDiary);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DiaryListAdapter(diaryList, mAQ);
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
        diaryList = DatabaseHelper.getInstance(this).getAllDiary();
        mAdapter.setDataSet(diaryList);
        mAdapter.notifyDataSetChanged();
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

    public void logI(String msg) {
        Log.i("AAAA", msg);
    }
}
