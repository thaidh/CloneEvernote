package com.example.thai.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.thai.myapplication.R;
import com.example.thai.myapplication.model.DiaryItem;
import com.example.thai.myapplication.ui.ComposeActivity;
import com.example.thai.myapplication.ui.DiaryListActivity;
import com.example.thai.myapplication.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by thaidh on 5/17/15.
 */
public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.ViewHolder> {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Context mContext;
    private ArrayList<DiaryItem> mDataSet;
    private Calendar cal;
    private AQuery mAQ;

    public DiaryListAdapter(ArrayList<DiaryItem> dataSet, AQuery aquery) {
        this.mAQ = aquery;
        this.mDataSet = dataSet;
        this.cal = Calendar.getInstance();
    }

    public void setDataSet(ArrayList<DiaryItem> dataSet) {
        this.mDataSet = dataSet;
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
        final DiaryItem diaryItem = mDataSet.get(i);
        cal.setTimeInMillis(diaryItem.getCreateTime());
        viewHolder.mTitle.setText(simpleDateFormat.format(cal.getTime()));
        if (TextUtils.isEmpty(diaryItem.getThumbPath())) {
            viewHolder.mImageView.setVisibility(View.GONE);
        } else {
            mAQ.id(viewHolder.mImageView).image(diaryItem.getThumbPath());
            viewHolder.mImageView.setVisibility(View.VISIBLE);
        }
        viewHolder.mImageView.setImageDrawable(Utils.getRandomDrawable(mContext, i));
        viewHolder.mContent.setText(diaryItem.getDescription());
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Log.i("AAAA", "Pos:" + pos);
                    Intent intent = new Intent(mContext, ComposeActivity.class);
                    intent.putExtra(DiaryListActivity.EXTRA_DIARY_CONTENT, diaryItem.getContent());
                            mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        public TextView mTitle;
        public ImageView mImageView;
        public TextView mContent;
        private ItemClickListener mItemClickListener;

        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tv_diary_title);
            mImageView = (ImageView) v.findViewById(R.id.imv_diary_preview);
            mContent = (TextView) v.findViewById(R.id.tv_diary_content);
            v.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        public void onItemClick(View v, int pos) ;
    }
}
