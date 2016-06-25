package com.artikov.photototext.notes;

import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.Date;

/**
 * Date: 23/6/2016
 * Time: 20:52
 *
 * @author Artur Artikov
 */
public class Note implements Serializable {
    private long mId;
    private String mName;
    private String mText;
    private Date mDate;

    public Note(long id, String name, String text, Date date) {
        mId = id;
        mName = name;
        mText = text;
        mDate = date;
    }

    public Note(String name, String text, Date date) {
        this(-1, name, text, date);
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public static class Database {
        public static final String TABLE = "Note";

        public static class Column {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String TEXT = "text";
            public static final String DATE = "date";
        }

        private static final String sCreateCommand = "create table " + TABLE + " ("
                + Column.ID + " integer primary key autoincrement,"
                + Column.NAME + " text,"
                + Column.TEXT + " text,"
                + Column.DATE + " text"  + ");";

        public static void onCreate(SQLiteDatabase db) {
            db.execSQL(sCreateCommand);
        }

        public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
