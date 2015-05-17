package com.example.thai.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thai.myapplication.R;
import com.example.thai.myapplication.utils.Utils;

/**
 * Created by thaidh on 5/17/15.
 */
public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.ViewHolder> {
    private Context mContext;
    private String[] mDataSet;

    public DiaryListAdapter(String[] dataSet) {
        this.mDataSet = dataSet;
    }

    public void setDataSet(String[] mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.mContext = viewGroup.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.diary_item_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final String data = mDataSet[i];
        viewHolder.mTextView.setText(mDataSet[i]);
        viewHolder.mImageView.setImageDrawable(Utils.getRandomDrawable(mContext, i));
        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Image: " + data , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_diary_title);
            mImageView = (ImageView) v.findViewById(R.id.imv_diary_preview);

        }


        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Data " + mTextView.getText() , Toast.LENGTH_SHORT).show();
        }
    }
}
