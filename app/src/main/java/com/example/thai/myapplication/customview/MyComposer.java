package com.example.thai.myapplication.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.internal.widget.ViewUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.example.thai.myapplication.R;
import com.example.thai.myapplication.database.DatabaseHelper;
import com.example.thai.myapplication.model.DiaryItem;
import com.example.thai.myapplication.utils.JsonUtils;
import com.example.thai.myapplication.utils.Utils;

import net.londatiga.android.QuickAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by CPU11307-local on 5/13/2015.
 */
public class MyComposer extends LinearLayout {
    private static final String TAG = MyComposer.class.getSimpleName();
    private CustomEditText.OnKeyDeleteListener onKeyDeleteListener;
    private ArrayList<View> viewContentArray = new ArrayList<View>();
    private QuickAction mQuickAction;
    private Random random = new Random();
    private String mContent;
    private EditText mEditText;
    private AQuery mAQ;

    private static final String IMAGE_SPAN = "<img src=\"%d\"/>";
    private static final String IMAGE_SPAN_REGEX = "<img src=\"(-?\\d+)\"/>";
    private static final Pattern imageSpanFinder = Pattern.compile(IMAGE_SPAN_REGEX);

    private static final ImageOptions noFileCache = new ImageOptions();

    public MyComposer(Context context) {
        this(context, null);
    }

    public MyComposer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAQ = new AQuery(context);
        noFileCache.fileCache = false;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);

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
                        removeView(curEditText);
                        viewContentArray.remove(curEditText);
                    } else if (previousView instanceof ImageView) {
                        removeView(previousView);
                        viewContentArray.remove(previousView);
                        if (TextUtils.isEmpty(curEditText.getText())) {
                            onKeyDeletePressed(curEditText);
                        }
                    }
                }
            }
        };
    }


    public void addPhotoToSelectedPosition(String path) {
        EditText selectedEditText = null;
        int selectedIndex = -1;
        String msgInNewEditText = "";
        // find selected view
        for (int i = 0; i < viewContentArray.size(); i++) {
            View view = viewContentArray.get(i);
            if (view instanceof EditText) {
                if (view.isFocused()) {
                    selectedEditText = ((EditText) view);
                    selectedIndex = i;
                    break;
                }
            }
        }

        //append image + edittext below
        if (selectedEditText != null && selectedIndex != -1) {
            String msg = selectedEditText.getText().toString();
            if (!TextUtils.isEmpty(msg)) {
                int breakIndex = selectedEditText.getSelectionStart();
                if (breakIndex > 0 && breakIndex <= msg.length()) {
                    msgInNewEditText = msg.substring(breakIndex);
                    selectedEditText.setText(msg.substring(0, breakIndex));
                } else {
                    msgInNewEditText = msg;
                    selectedEditText.setText("");
                }
            }

            int newImageIndex = selectedIndex + 1;
            ImageView imageView = createImageView(path);
            viewContentArray.add(newImageIndex, imageView);
            addView(imageView, newImageIndex);

            int newEditTextIndex = newImageIndex + 1;
            EditText customEditText = createEditText(onKeyDeleteListener);
            customEditText.setText(msgInNewEditText);
            viewContentArray.add(newEditTextIndex, customEditText);
            addView(customEditText, newEditTextIndex);
        }
    }

    private EditText createEditText(CustomEditText.OnKeyDeleteListener onKeyDeleteListener) {
        CustomEditText customEditText = new CustomEditText(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        customEditText.setLayoutParams(layoutParams);
        customEditText.setTextColor(getResources().getColor(R.color.primary_color));
        customEditText.setBackgroundColor(Color.TRANSPARENT);
        customEditText.requestFocus();
        customEditText.setOnKeyDeleteListener(onKeyDeleteListener);
        return customEditText;
    }


    private ImageView createImageView(String localPath) {
        ImageView imageView = new ImageView(getContext());
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Utils.convertDpToPixel(200, getContext()), Utils.convertDpToPixel(200, getContext()));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = Utils.convertDpToPixel(20, getContext());
        layoutParams.setMargins(margin, margin, margin, margin);
        imageView.setLayoutParams(layoutParams);
        imageView.setTag(localPath);
        mAQ.id(imageView).image(localPath, new ImageOptions());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setTag(localPath);
        return imageView;
    }

    public void setQuickAction(QuickAction mQuickAction) {
        this.mQuickAction = mQuickAction;
    }


    private static final int EDIT_TEXT_TYPE = 1;
    private static final int IMAGE_VIEW_TYPE = 2;


    public void exportData() {

        try {
            boolean isSetDesc = false;
            boolean isSetThumb = false;
            DiaryItem item = new DiaryItem();
            JSONArray jsonContent = new JSONArray();
            for (View view : viewContentArray) {
                if (view instanceof EditText) {
                    JSONObject jsonItemEditText = new JSONObject();
                    jsonItemEditText.put(JsonUtils.TYPE, EDIT_TEXT_TYPE);
                    CharSequence curText = ((EditText) view).getText();
                    jsonItemEditText.put(JsonUtils.CONTENT, curText);
                    jsonContent.put(jsonItemEditText);
                    if (!isSetDesc && !TextUtils.isEmpty(curText)) {
                        item.setDescription(curText.toString());
                        isSetDesc = true;
                    }

                } else if (view instanceof ImageView) {
                    String imagePath = (String) ((ImageView) view).getTag();
                    JSONObject jsonItemImageView = new JSONObject();
                    jsonItemImageView.put(JsonUtils.TYPE, IMAGE_VIEW_TYPE);
                    jsonItemImageView.put(JsonUtils.CONTENT, imagePath);
                    jsonContent.put(jsonItemImageView);
                    if (!isSetThumb) {
                        item.setThumbPath(imagePath);
                        isSetThumb = true;
                    }
                }
            }
            item.setContent(jsonContent.toString());
            item.setCreateTime(System.currentTimeMillis());
            DatabaseHelper.getInstance(getContext()).insertDiary(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importData(String jsonContent) {
        try {
            JSONArray array = new JSONArray(jsonContent);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonItem = (JSONObject) array.get(i);
                int itemType = jsonItem.getInt(JsonUtils.TYPE);
                switch (itemType) {
                    case EDIT_TEXT_TYPE:
                        String itemContent = jsonItem.getString(JsonUtils.CONTENT);
                        EditText editText = createEditText(onKeyDeleteListener);
                        editText.setText(itemContent);
                        viewContentArray.add(editText);
                        addView(editText);
                        break;
                    case IMAGE_VIEW_TYPE:
                        String path = jsonItem.getString(JsonUtils.CONTENT);
                        ImageView imageView = createImageView(path);
                        viewContentArray.add(imageView);
                        addView(imageView);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initNewDiary() {
        EditText customEditText = createEditText(onKeyDeleteListener);
        viewContentArray.add(customEditText);
        addView(customEditText);
    }
}
