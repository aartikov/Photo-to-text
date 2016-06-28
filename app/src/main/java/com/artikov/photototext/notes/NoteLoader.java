package com.artikov.photototext.notes;

import android.content.Context;
import android.os.AsyncTask;

import com.artikov.photototext.notes.db.NoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 24/6/2016
 * Time: 19:10
 *
 * @author Artur Artikov
 */
public class NoteLoader {
    private Context mContext;
    private Listener mListener;
    private NoteAsyncTask mAsyncTask;
    private List<Note> mNotes = new ArrayList<>();

    public interface Listener {
        void showProgress();

        void hideProgress();

        void setResult(List<Note> notes);
    }

    public NoteLoader(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setListener(Listener listener) {
        mListener = listener;
        if (isRunning()) mListener.showProgress();
        else mListener.setResult(mNotes);
    }

    public void load(String searchString) {
        cancel();
        mAsyncTask = new NoteAsyncTask();
        mAsyncTask.execute(searchString);
        mListener.showProgress();
    }

    public void cancel() {
        if (isRunning()) mAsyncTask.cancel(true);
    }

    public boolean isRunning() {
        return mAsyncTask != null;
    }

    public class NoteAsyncTask extends AsyncTask<String, Void, List<Note>> {

        @Override
        protected List<Note> doInBackground(String... params) {
            String searchString = params[0];
            NoteDataSource dataSource = new NoteDataSource(mContext);
            return searchString.isEmpty() ? dataSource.getAll() : dataSource.search(searchString);
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            super.onPostExecute(notes);
            mAsyncTask = null;
            mNotes = notes;
            mListener.hideProgress();
            mListener.setResult(mNotes);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (mAsyncTask == this) {
                mAsyncTask = null;
                mListener.hideProgress();
            }
        }
    }

}
