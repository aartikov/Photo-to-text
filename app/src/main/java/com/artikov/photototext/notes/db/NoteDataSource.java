package com.artikov.photototext.notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.artikov.photototext.notes.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Date: 25/6/2016
 * Time: 13:51
 *
 * @author Artur Artikov
 */

public class NoteDataSource {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.mm.yyyy hh:mm", Locale.getDefault());
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;

    public NoteDataSource(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public void close() {
        mDatabaseHelper.close();
        mDatabase = null;
    }

    public List<Note> getAll() {
        if(mDatabase == null) throw new IllegalStateException("Database is not opened");

        List<Note> notes = new ArrayList<>();
        Cursor cursor = mDatabase.query(Note.Database.TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Note note = converCursorToNote(cursor);
            notes.add(note);
            cursor.moveToNext();
        }
        cursor.close();
        return notes;
    }

    public void add(Note note) {
        if(mDatabase == null) throw new IllegalStateException("Database is not opened");

        ContentValues values = new ContentValues();
        values.put(Note.Database.Column.NAME, note.getName());
        values.put(Note.Database.Column.TEXT, note.getText());
        values.put(Note.Database.Column.DATE, DATE_FORMAT.format(note.getDate()));

        long id = mDatabase.insert(Note.Database.TABLE, null, values);
        note.setId(id);
    }

    public void delete(Note note) {
        if(mDatabase == null) throw new IllegalStateException("Database is not opened");

        mDatabase.delete(Note.Database.TABLE, Note.Database.Column.ID + " = ?", new String[]{String.valueOf(note.getId())});
    }

    private Note converCursorToNote(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(Note.Database.Column.ID);
        int nameIndex = cursor.getColumnIndex(Note.Database.Column.NAME);
        int textIndex = cursor.getColumnIndex(Note.Database.Column.TEXT);
        int dateIndex = cursor.getColumnIndex(Note.Database.Column.DATE);

        long id = cursor.getLong(idIndex);
        String name = cursor.getString(nameIndex);
        String text = cursor.getString(textIndex);
        Date date;
        try {
            date = DATE_FORMAT.parse(cursor.getString(dateIndex));
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }

        return new Note(id, name, text, date);
    }
}
