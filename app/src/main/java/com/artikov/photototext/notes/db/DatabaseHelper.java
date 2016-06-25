package com.artikov.photototext.notes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.artikov.photototext.notes.Note;

/**
 * Date: 25/6/2016
 * Time: 13:38
 *
 * @author Artur Artikov
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NAME = "NOTE_DATABASE";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Note.Database.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Note.Database.onUpgrade(db, oldVersion, newVersion);
    }
}
