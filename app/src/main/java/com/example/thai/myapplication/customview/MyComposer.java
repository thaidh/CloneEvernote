package com.example.thai.myapplication.customview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.thai.myapplication.R;
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

    private static final String IMAGE_SPAN = "<img src=\"%d\"/>";
    private static final String IMAGE_SPAN_REGEX = "<img src=\"(-?\\d+)\"/>";
    private static final Pattern imageSpanFinder = Pattern.compile(IMAGE_SPAN_REGEX);




    public MyComposer(Context context) {
        this(context, null);
    }

    public MyComposer(Context context, AttributeSet attrs) {
        super(context, attrs);
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


    public void addPhotoToSelectedPosition() {
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
            ImageView imageView = createImageView();
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

    private ImageView createImageView() {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        int sourceDrawable = random.nextInt(1000) + 1;
        imageView.setImageDrawable(Utils.getRandomDrawable(getContext(), sourceDrawable));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setTag(sourceDrawable);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mQuickAction != null) {
                    mQuickAction.show(v);
                }
                return false;
            }
        });
        return imageView;
    }



    public void setQuickAction(QuickAction mQuickAction) {
        this.mQuickAction = mQuickAction;
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
    //todo may be used

    private static final int EDIT_TEXT_TYPE = 1;
    private static final int IMAGE_VIEW_TYPE = 2;



    public String exportData() {
        JSONArray jsonContent = new JSONArray();
        try {
            for (View view : viewContentArray) {
                if (view instanceof  EditText) {
                    JSONObject jsonItemEditText = new JSONObject();
                    jsonItemEditText.put(JsonUtils.TYPE, EDIT_TEXT_TYPE);
                    jsonItemEditText.put(JsonUtils.CONTENT, ((EditText) view).getText());
                    jsonContent.put(jsonItemEditText);

                } else if (view instanceof ImageView) {
                    JSONObject jsonItemImageView = new JSONObject();
                    jsonItemImageView.put(JsonUtils.TYPE, IMAGE_VIEW_TYPE);
                    jsonItemImageView.put(JsonUtils.CONTENT, ((ImageView) view).getTag());
                    jsonContent.put(jsonItemImageView);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonContent.toString();
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
                        ImageView imageView = createImageView();
                        viewContentArray.add(imageView);
                        addView(imageView);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void initNewDiary() {
        EditText customEditText = createEditText(onKeyDeleteListener);
        viewContentArray.add(customEditText);
        addView(customEditText);
    }
}
