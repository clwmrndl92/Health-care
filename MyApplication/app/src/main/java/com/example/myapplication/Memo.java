package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;


public class Memo extends RealmObject implements Parcelable {
    private String title;
    private String content;
    private long time;
    private boolean isChecked = false;

    public Memo(){
        //date = new Date();
        time = new Date().getTime();
        title = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(time);
        content = "no content";
    }

    public Memo(Parcel src){
        time = src.readLong();
        title = src.readString();
        content = src.readString();
    }

    public Memo(String title) {
        time = new Date().getTime();
        this.title = title;
        content = "no content";
    }

    public Memo(long time, String title){
        this.time = time;
        this.title = title;
        content = "no content";
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(title);
        dest.writeString(content);
    }

    public static final Creator<Memo> CREATOR = new Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel in) {
            return new Memo(in);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };
}
