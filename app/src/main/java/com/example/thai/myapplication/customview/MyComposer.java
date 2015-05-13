package com.example.thai.myapplication.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.thai.myapplication.R;

import net.londatiga.android.QuickAction;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by CPU11307-local on 5/13/2015.
 */
public class MyComposer extends LinearLayout {
    private static final String TAG = MyComposer.class.getSimpleName();
    private CustomEditText.OnKeyDeleteListener onKeyDeleteListener;
    private ArrayList<View> viewContentArray = new ArrayList<View>();
    private QuickAction mQuickAction;
    private Random random = new Random();

    public MyComposer(Context context) {
        super(context);
    }

    public MyComposer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setOrientation(VERTICAL);
        EditText customEditText = createEditText(onKeyDeleteListener);
        viewContentArray.add(customEditText);
        addView(customEditText);

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
        customEditText.setTextColor(Color.WHITE);
        customEditText.setBackgroundColor(Color.TRANSPARENT);
        customEditText.requestFocus();
        customEditText.setOnKeyDeleteListener(onKeyDeleteListener);
        return customEditText;
    }

    private ImageView createImageView() {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(getRandomDrawable(random.nextInt(1000) + 1));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

    private Drawable getRandomDrawable(int random) {

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

    public void setQuickAction(QuickAction mQuickAction) {
        this.mQuickAction = mQuickAction;
    }
}
