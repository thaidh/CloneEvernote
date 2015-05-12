package com.example.thai.myapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.thai.myapplication.CustomImageSpan;
import com.example.thai.myapplication.R;
import com.example.thai.myapplication.customview.CustomEditText;

import net.londatiga.android.ActionItem;
import net.londatiga.android.QuickAction;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;


public class ComposeActivity extends Activity {
    private static final String TAG = ComposeActivity.class.getSimpleName();
    private static final int ID_ADD = 1;
    private static final int ID_ACCEPT = 2;
    private static final int ID_UPLOAD = 3;

    private Button btnText;
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
    private LinearLayout mContent;
    private CustomEditText.OnKeyDeleteListener onKeyDeleteListener;

    private ArrayList<View> viewContentArray = new ArrayList<View>();
    private Random random = new Random();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Point size = new Point();
            windowManager.getDefaultDisplay().getSize(size);
            mScreenWidth = size.x;
            mScreenHeight = size.y;
            mImageSpanWidth = mScreenWidth - (2 * getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin))
                    - (2 * getResources().getDimensionPixelSize(R.dimen.default_padding));


            ActionItem addItem 		= new ActionItem(ID_ADD, "Add", getResources().getDrawable(R.drawable.ic_add));
            ActionItem acceptItem 	= new ActionItem(ID_ACCEPT, "Accept", getResources().getDrawable(R.drawable.ic_accept));
            ActionItem uploadItem 	= new ActionItem(ID_UPLOAD, "Upload", getResources().getDrawable(R.drawable.ic_up));

            //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
            uploadItem.setSticky(true);

            mQuickAction 	= new QuickAction(this);

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
            mContent = (LinearLayout) findViewById(R.id.diary_content);

            onKeyDeleteListener = new CustomEditText.OnKeyDeleteListener() {
                @Override
                public void onKeyDeletePressed(EditText curEditText) {
                    Log.i(TAG, "On key delete in");
                    int previousIndex = viewContentArray.indexOf(curEditText) - 1;
                    if (previousIndex >= 0) {
                        View previousView = viewContentArray.get(previousIndex);
                        if (previousView instanceof EditText) {
                            EditText editText = (EditText)previousView;
                            Log.i(TAG, "Merge edit text");
                            if (!TextUtils.isEmpty(curEditText.getText())) {
                                editText.append(curEditText.getText());
                            }
                            previousView.requestFocus();
                            editText.setSelection(editText.getText().length());
                            mContent.removeView(curEditText);
                            viewContentArray.remove(curEditText);
                        } else if (previousView instanceof  ImageView) {
                            mContent.removeView(previousView);
                            viewContentArray.remove(previousView);
                            if (TextUtils.isEmpty(curEditText.getText())) {
                                onKeyDeletePressed(curEditText);
                            }
                            Toast.makeText(getApplicationContext(), "Delete image view", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            };

            btnText = (Button) findViewById(R.id.btnAppendPhoto);
            btnText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText customEditText = createEditText(onKeyDeleteListener);
                    viewContentArray.add(customEditText);
                    mContent.addView(customEditText);
                }
            });


            btnSave = (Button) findViewById(R.id.btnSave);
            btnSave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    EditText selectedEditText = null;
                    for (View view : viewContentArray) {
                        if (view instanceof EditText) {
                            if (view.isFocused()) {
                                selectedEditText = ((EditText) view);
                                break;
                            }
                        }
                    }
                    String msgInNewEditText = "";
                    if (selectedEditText != null) {
                        String msg = selectedEditText.getText().toString();
                        if (!TextUtils.isEmpty(msg)) {
                            int breakIndex = selectedEditText.getSelectionStart();
                            if (breakIndex > 0 && breakIndex < (msg.length() - 1)) {
                                msgInNewEditText = msg.substring(breakIndex);
                                selectedEditText.setText(msg.substring(0, breakIndex));
                            } else {
                                msgInNewEditText = msg;
                                selectedEditText.setText("");
                            }
                        }
                    }

                    ImageView imageView = createImageView();
                    viewContentArray.add(imageView);
                    mContent.addView(imageView);


                    EditText customEditText = createEditText(onKeyDeleteListener);
                    customEditText.setText(msgInNewEditText);
                    viewContentArray.add(customEditText);
                    mContent.addView(customEditText);
                }
            });

            btnLoad = (Button) findViewById(R.id.btnLoad);
            btnLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public EditText createEditText(CustomEditText.OnKeyDeleteListener onKeyDeleteListener) {
        CustomEditText customEditText = new CustomEditText(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        customEditText.setLayoutParams(layoutParams);
        customEditText.setBackgroundColor(Color.TRANSPARENT);
//        customEditText.setText("ABCDEFGHG");
        customEditText.requestFocus();
        customEditText.setOnKeyDeleteListener(onKeyDeleteListener);
        return customEditText;
    }

    public ImageView createImageView() {
        ImageView imageView = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setImageDrawable(getRandomDrawable(random.nextInt(1000) + 1));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
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
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Drawable getRandomDrawable(int random) {
//        int random = new Random().nextInt();

        switch (random % 4) {
            case 1:
                return getResources().getDrawable(R.drawable.demo1);
            case 2:
                return getResources().getDrawable(R.drawable.demo2);

            case 3:
                return getResources().getDrawable(R.drawable.demo3);

            default:
                return getResources().getDrawable(R.drawable.demo4);
        }
    }

    private CustomImageSpan createImageSpan(int value) {
        CustomImageSpan imageSpan = new CustomImageSpan(getApplicationContext(), drawableToBitmap(getRandomDrawable(value), mImageSpanWidth, 200), ImageSpan.ALIGN_BOTTOM);
        imageSpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mQuickAction.show(v);
            }
        });
        return imageSpan;
    }

    public Bitmap drawableToBitmap(Drawable drawable, int width, int height) {
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
//        final int width = !drawable.getBounds().isEmpty() ?
//                drawable.getBounds().width() : drawable.getIntrinsicWidth();
//
//        final int height = !drawable.getBounds().isEmpty() ?
//                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        int mMarginTop = 10;
        int mMarginBackground = 10;
        Drawable background = getResources().getDrawable(R.drawable.thumb1);
        background.setBounds(0, mMarginTop, canvas.getWidth(), canvas.getHeight() - mMarginTop);
        background.draw(canvas);
//        canvas.drawRect(0, mMarginTop, canvas.getWidth(), canvas.getHeight() - mMarginTop, paint);
        drawable.setBounds(mMarginBackground, mMarginBackground + mMarginTop, canvas.getWidth() - mMarginBackground, canvas.getHeight() - (mMarginBackground + mMarginTop));
        drawable.draw(canvas);

        return bitmap;
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
