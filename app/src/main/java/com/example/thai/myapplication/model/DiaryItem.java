package com.example.thai.myapplication.model;

/**
 * Created by Thai on 4/19/2015.
 */
public class DiaryItem {
    public int id;
    public String content;
    public long createTime;

    public DiaryItem() {
        this.id = -1;
        this.content = "";
        this.createTime = -1;
    }

    public DiaryItem(int id, String content, long createTime) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
    }
}