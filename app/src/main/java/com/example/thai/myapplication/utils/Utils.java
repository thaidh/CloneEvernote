package com.example.thai.myapplication.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.thai.myapplication.R;

/**
 * Created by thaidh on 5/17/15.
 */
public class Utils {

    public static Drawable getRandomDrawable(Context context, int random) {

        switch (random % 4) {
            case 1:
                return context.getResources().getDrawable(R.drawable.demo1);
            case 2:
                return context.getResources().getDrawable(R.drawable.demo2);

            case 3:
                return context.getResources().getDrawable(R.drawable.demo3);

            default:
                return context.getResources().getDrawable(R.drawable.demo4);
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
    //todo may be used
}
