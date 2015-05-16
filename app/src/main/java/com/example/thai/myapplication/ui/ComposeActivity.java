package com.example.thai.myapplication.ui;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.thai.myapplication.R;
import com.example.thai.myapplication.customview.MyComposer;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;

import java.util.regex.Pattern;


public class ComposeActivity extends ActionBarActivity {
    private static final String TAG = ComposeActivity.class.getSimpleName();
    private static final int ID_ADD = 1;
    private static final int ID_ACCEPT = 2;
    private static final int ID_UPLOAD = 3;

    private Button btnPhoto;
    private Button btnSave;
    private Button btnLoad;
    private ScrollView scrollView;


    private static final String IMAGE_SPAN = "<img src=\"%d\"/>";
    private static final String IMAGE_SPAN_REGEX = "<img src=\"(-?\\d+)\"/>";
    private static final Pattern imageSpanFinder = Pattern.compile(IMAGE_SPAN_REGEX);

    private int mScreenWidth;
    private int mScreenHeight;
    private int mImageSpanWidth;

    private QuickAction mQuickAction;
    private MyComposer mComposer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_new_note);

            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Point size = new Point();
            windowManager.getDefaultDisplay().getSize(size);
            mScreenWidth = size.x;
            mScreenHeight = size.y;
            mImageSpanWidth = mScreenWidth - (2 * getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin)) - (2 * getResources().getDimensionPixelSize(R.dimen.default_padding));

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

            scrollView = (ScrollView) findViewById(R.id.scrollView);
            mComposer = (MyComposer) findViewById(R.id.diary_content);
            mComposer.setQuickAction(mQuickAction);

            btnPhoto = (Button) findViewById(R.id.btnAppendPhoto);
            btnPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mComposer.addPhotoToSelectedPosition();
                }
            });


            btnSave = (Button) findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_SHORT).show();
                }
            });

            btnLoad = (Button) findViewById(R.id.btnLoad);
            btnLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Load", Toast.LENGTH_SHORT).show();
                }
            });

            setupToolbar();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Compose Diary");
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
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

//            etMessage = (EditText) findViewById(R.id.etMessage);
//            etMessage.setBackgroundColor(Color.TRANSPARENT);
//            etMessage.setTextColor(Color.GRAY);
//            etMessage.requestFocus();
//            etMessage.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    Log.i("AAAAA","Before text change:" + s + " " + start + " " + count);
//                    int selectPos = etMessage.getSelectionStart();
//
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editTable) {
//                    Matcher matcher = imageSpanFinder.matcher(editTable);
//
//                    int i = 0;
//                    ImageSpan[] arrayOfImageSpan = editTable.getSpans(0, editTable.length(), ImageSpan.class);
//                    int j = arrayOfImageSpan.length;
//                    while (true) {
//                        if (i >= j) {
//                            int posToFind = 0;
//                            while (matcher.find(posToFind)) {
//                                int start = matcher.start();
//                                int end = matcher.end();
//                                int value = Integer.parseInt(matcher.group(1));
//
//                                posToFind = end < (editTable.length()-1) ? end : (editTable.length()-1);
//
//                                editTable.setSpan(createImageSpan(value), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            }
//
//                            break;
//                        }
//                        editTable.removeSpan(arrayOfImageSpan[i]);
//                        i++;
//                    }
//
//
//                }
//            });
