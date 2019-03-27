package com.example.memo;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Memo implements Serializable {

    private int memoId;
    private Date date;
    private String text;
    private int priority;
    private boolean fullDisplayed;
    private static DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyy hh:mm:ss a");

    Calendar calendar;
    String dateStr;

    public Memo() {
        this.date = new Date();

    }


    public Memo(long time, String text, int priority) {
        this.date = new Date(time);
        this.text=text;
        this.priority = priority;


    }


    public int getMemoId() {
        return memoId;
    }

    public void setMemoId(int memoId) {
        this.memoId = memoId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getTime() {
        return date.getTime();
    }

    public void setTime(long time) {
        this.date = new Date(time);
    }

    public String getDate() {
        return dateFormat.format(date);
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



