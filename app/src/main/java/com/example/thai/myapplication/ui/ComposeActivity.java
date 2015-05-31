package com.example.thai.myapplication.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.thai.myapplication.R;
import com.example.thai.myapplication.customview.MyComposer;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;


public class ComposeActivity extends ActionBarActivity {
    private static final String TAG = ComposeActivity.class.getSimpleName();
    private static final int ID_ADD = 1;
    private static final int ID_ACCEPT = 2;
    private static final int ID_UPLOAD = 3;

    private static int LOAD_IMAGE_RESULTS = 1;

    private Button btnPhoto;
    private ScrollView scrollView;

    private QuickAction mQuickAction;
    private MyComposer mComposer;
    private String mJsonContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_note);

            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(DiaryListActivity.EXTRA_DIARY_CONTENT)) {
                mJsonContent = intent.getStringExtra(DiaryListActivity.EXTRA_DIARY_CONTENT);
            }

            scrollView = (ScrollView) findViewById(R.id.scrollView);
            mComposer = (MyComposer) findViewById(R.id.diary_content);
            if (TextUtils.isEmpty(mJsonContent)) {
                mComposer.initNewDiary();
            } else {
                mComposer.importData(mJsonContent);
            }

            btnPhoto = (Button) findViewById(R.id.btnAppendPhoto);
            btnPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mComposer.addPhotoToSelectedPosition();
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
                    startActivityForResult(i, LOAD_IMAGE_RESULTS);
                }
            });

            setupToolbar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Compose");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void scrollToBottom() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        }, 200);
    }

    private void setupQuickActionItem() {
        ActionItem addItem = new ActionItem(ID_ADD, "Add", getResources().getDrawable(R.drawable.ic_add));
        ActionItem acceptItem = new ActionItem(ID_ACCEPT, "Accept", getResources().getDrawable(R.drawable.ic_accept));
        ActionItem uploadItem = new ActionItem(ID_UPLOAD, "Upload", getResources().getDrawable(R.drawable.ic_up));

        //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
        uploadItem.setSticky(true);

        mQuickAction = new QuickAction(this);

        mQuickAction.addActionItem(addItem);
        mQuickAction.addActionItem(acceptItem);
        mQuickAction.addActionItem(uploadItem);

        //setup the action item click listener
        mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction quickAction, int pos, int actionId) {
                ActionItem actionItem = quickAction.getActionItem(pos);

                if (actionId == ID_ADD) {
                    Toast.makeText(getApplicationContext(), "Add item selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), actionItem.getTitle() + " selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case  android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                mComposer.exportData();
                finish();
                return true;
            case R.id.action_attach:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {
                // Let's read picked image data - its URI
                Uri pickedImage = data.getData();
                // Let's read picked image path using content resolver
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                logI(imagePath);
                mComposer.addPhotoToSelectedPosition(imagePath);
//                // Now we need to set the GUI ImageView data with data read from the picked file.
//                image.setImageBitmap(BitmapFactory.decodeFile(imagePath));
//
//                // At the end remember to close the cursor or you will end with the RuntimeException!
//                cursor.close();
            }
    }

    public void logI(String msg) {
        Log.i("AAAA", msg);
    }
}


