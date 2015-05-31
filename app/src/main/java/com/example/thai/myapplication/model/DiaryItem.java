package com.example.thai.myapplication.model;

/**
 * Created by Thai on 4/19/2015.
 */
public class DiaryItem {
    private int id;
    private String content;
    private long createTime;
    private String description;
    private String thumbPath;

    public DiaryItem() {
        this.id = -1;
        this.content = "";
        this.createTime = -1;
        this.description = "";
        this.thumbPath = "";
    }

    public DiaryItem(int id, String content, long createTime, String des, String thumb) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.description = des;
        this.thumbPath = thumb;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }
}