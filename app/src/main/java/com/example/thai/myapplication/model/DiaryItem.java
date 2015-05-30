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

    public DiaryItem(String content, long createTime) {
        this.content = content;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}