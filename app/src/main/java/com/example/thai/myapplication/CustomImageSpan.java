package com.example.thai.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

/**
 * Created by CPU11307-local on 4/13/2015.
 */
public class CustomImageSpan extends ImageSpan {
    private static final String TAG = CustomImageSpan.class.getName();
    private Paint mPaint;
    private int mPadding;
    private View.OnClickListener onClickListener;

    public CustomImageSpan(Context context, Bitmap bmp, int verticalAlignment) {
        super(context, bmp, verticalAlignment);
        initData();
    }

    private void initData() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPadding = 20;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
//        Log.i(TAG, "Start: " + start);
//        Log.i(TAG, "End: "  + end);
//        Log.i(TAG, "Top: " + top);
//        Log.i(TAG, "Bottom: " + bottom);
//        Log.i(TAG, "X: " + x);
//        Log.i(TAG, "Y: " + y);
//
//        Log.i(TAG, "Width: " + canvas.getWidth());
//        Log.i(TAG, "Height" + canvas.getHeight());
//        int width = canvas.getWidth();
//        int height = canvas.getHeight();
//        int newStart = start + mPadding;
//        int newEnd = end - mPadding;
//        int newTop = top + mPadding;
//        int newBottom = bottom - mPadding;
//        float newX = x + mPadding;
//        int newY = y - mPadding;
//        canvas.drawRect(0, 0, width,height, mPaint);
//        super.draw(canvas, text, newStart, newEnd, newX, newTop, newY, newBottom, paint);
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
//        canvas.drawText("ABCD", 0, canvas.getHeight(), mPaint);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void onClick(View view) {
       Log.i(TAG, "On click span");
        if (onClickListener != null) {
            onClickListener.onClick(view);
        }
    }
 }
