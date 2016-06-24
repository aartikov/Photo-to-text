package com.artikov.photototext.notes;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Date: 24/6/2016
 * Time: 19:10
 *
 * @author Artur Artikov
 */
public class NoteLoader {
    private static final int NOTES_COUNT = 10;
    private Listener mListener;
    private NoteAsyncTask mAsyncTask;
    private List<Note> mNotes = new ArrayList<>();

    public interface Listener {
        void showProgress();

        void hideProgress();

        void updateProgress(int progress);

        void setResult(List<Note> notes);
    }

    public NoteLoader(Listener listener) {
        mListener = listener;
    }

    public void setListener(Listener listener) {
        mListener = listener;
        if(isRunning()) mListener.showProgress();
        else mListener.setResult(mNotes);
    }

    public void load() {
        cancel();
        mAsyncTask = new NoteAsyncTask();
        mAsyncTask.execute();
        mListener.showProgress();
    }

    public void cancel() {
        if(isRunning()) mAsyncTask.cancel(true);
    }

    public boolean isRunning() {
        return mAsyncTask != null;
    }

    public class NoteAsyncTask extends AsyncTask<Void, Integer, List<Note>> {

        @Override
        protected List<Note> doInBackground(Void... params) {
            List<Note> notes = new ArrayList<>(NOTES_COUNT);
            for (int i = 0; i < NOTES_COUNT; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(5000 / NOTES_COUNT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return new ArrayList<>();
                }
                notes.add(new Note("Name " + i, "Text\nTeeext\nTeeeeext " + i, new Date()));
                publishProgress((i + 1) * 100 / NOTES_COUNT);
            }
            return notes;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mListener.updateProgress(values[0]);
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
            if(mAsyncTask == this) {
                mAsyncTask = null;
                mListener.hideProgress();
            }
        }
    }

}
