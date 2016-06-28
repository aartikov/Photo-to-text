package com.artikov.photototext.notes;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

/**
 * Date: 23/6/2016
 * Time: 20:52
 *
 * @author Artur Artikov
 */

@DatabaseTable(tableName = Note.Database.TABLE)
public class Note implements Serializable {
    @DatabaseField(columnName = Database.Column.ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = Database.Column.NAME)
    private String mName;

    @DatabaseField(columnName = Database.Column.TEXT)
    private String mText;

    @DatabaseField(columnName = Database.Column.DATE)
    private Date mDate;

    public Note() {
    }

    public Note(int id, String name, String text, Date date) {
        mId = id;
        mName = name;
        mText = text;
        mDate = date;
    }

    public Note(String name, String text, Date date) {
        this(-1, name, text, date);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
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

        static public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) throws SQLException {
            TableUtils.createTable(connectionSource, Note.class);
        }

        static public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) throws SQLException {
        }
    }
}
