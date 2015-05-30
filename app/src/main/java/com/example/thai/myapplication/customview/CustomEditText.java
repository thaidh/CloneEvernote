package com.example.thai.myapplication.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by CPU11307-local on 5/12/2015.
 */
public class CustomEditText extends EditText {
    private static final String TAG = CustomEditText.class.getSimpleName();

    private OnKeyDeleteListener onKeyDeleteListener;
    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        Log.i("AAA", "Key hit: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN && getSelectionStart() == 0) {
            if (onKeyDeleteListener != null) {
                onKeyDeleteListener.onKeyDeletePressed(this);
                return true;
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }


    public void setOnKeyDeleteListener(OnKeyDeleteListener onKeyDeleteListener) {
        this.onKeyDeleteListener = onKeyDeleteListener;
    }

    public interface OnKeyDeleteListener {
        void onKeyDeletePressed(EditText et);
    }
}
