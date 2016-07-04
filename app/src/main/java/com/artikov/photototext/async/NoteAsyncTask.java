package com.artikov.photototext.async;

import android.content.Context;
import android.os.AsyncTask;

import com.artikov.photototext.data.Note;
import com.artikov.photototext.db.NoteDataSource;

import java.util.List;

/**
 * Date: 4/7/2016
 * Time: 16:03
 *
 * @author Artur Artikov
 */
public class NoteAsyncTask extends AsyncTask<String, Void, List<Note>> {
    private Context mContext;
    private Listener mListener;

    public interface Listener {
        void onLoaded(List<Note> notes);
    }

    public NoteAsyncTask(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected List<Note> doInBackground(String... params) {
        NoteDataSource dataSource = new NoteDataSource(mContext);
        return params.length == 0 ? dataSource.getAll() : dataSource.search(params[0]);
    }

    @Override
    protected void onPostExecute(List<Note> notes) {
        super.onPostExecute(notes);
        mListener.onLoaded(notes);
    }
}