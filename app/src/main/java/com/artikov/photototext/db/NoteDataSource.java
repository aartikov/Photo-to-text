package com.artikov.photototext.db;

import android.content.Context;

import com.artikov.photototext.data.Note;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Date: 25/6/2016
 * Time: 13:51
 *
 * @author Artur Artikov
 */

public class NoteDataSource {
    private DatabaseHelper mDatabaseHelper;

    public NoteDataSource(Context context) {
        mDatabaseHelper = DatabaseHelper.getInstance(context);
    }

    public List<Note> queryNotes(String query) {
        try {
            if (query.isEmpty()) {
                return mDatabaseHelper.getNoteDao().queryBuilder()
                        .orderBy(Note.Database.Column.ID, false).query();
            }
            String searchTemplate = "%" + query + "%";
            return mDatabaseHelper.getNoteDao().queryBuilder()
                    .orderBy(Note.Database.Column.ID, false)
                    .where().like(Note.Database.Column.NAME, searchTemplate)
                    .or().like(Note.Database.Column.TEXT, searchTemplate)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void create(Note note) {
        try {
            mDatabaseHelper.getNoteDao().create(note);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Note note) {
        try {
            mDatabaseHelper.getNoteDao().update(note);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Note note) {
        try {
            mDatabaseHelper.getNoteDao().delete(note);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
