package com.artikov.photototext.notes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.artikov.photototext.notes.Note;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Date: 25/6/2016
 * Time: 13:38
 *
 * @author Artur Artikov
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String NAME = "NOTE_DATABASE";
    private static final int VERSION = 1;
    private static DatabaseHelper sInstance;

    private Dao<Note, Integer> mNoteDao;

    private DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    static public DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    public Dao<Note, Integer> getNoteDao() throws SQLException {
        if (mNoteDao == null) {
            mNoteDao = getDao(Note.class);
        }
        return mNoteDao;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Note.Database.onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Note.Database.onUpgrade(database, connectionSource, oldVersion, newVersion);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
