package com.example.memo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Memo implements Serializable {

    private int memoId;
    private String date;
    private String text;
    private String importance;
    private boolean fullDisplayed;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy hh:mm:ss a");

    Calendar calendar;
    String dateStr;

    public Memo() {

    }


    public Memo(String text, String importance) {

        this.text = text;
        this.importance = importance;

        calendar = Calendar.getInstance();
        dateStr = dateFormat.format(calendar.getTime());

    }


    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public void setDate(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getDate() {
        return dateStr;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public String getShortText() {
        String temp = text.replaceAll("\n", " ");
        if (temp.length() > 25) {
            return temp.substring(0, 25) + "...";
        } else {
            return temp;
        }
    }

    public void setFullDisplayed(boolean fullDisplayed) {
        this.fullDisplayed = fullDisplayed;
    }

    public boolean isFullDisplayed() {
        return this.fullDisplayed;
    }
    @Override
    public String toString() {
        return this.text;
    }
}



