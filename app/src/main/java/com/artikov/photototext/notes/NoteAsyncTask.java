package com.artikov.photototext.notes;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Date: 24/6/2016
 * Time: 13:03
 *
 * @author Artur Artikov
 */

public class NoteAsyncTask extends AsyncTask<Void, Integer, List<Note>> {
    private static final int NOTES_COUNT = 10;
    private Listener mListener;
    private List<Note> mResult = new ArrayList<>();

    public interface Listener {
        void showProgress();

        void hideProgress();

        void updateProgress(int progress);

        void setResult(List<Note> notes);
    }

    public NoteAsyncTask(Listener listener) {
        mListener = listener;
    }

    public void setListener(Listener listener) {
        mListener = listener;

        switch (getStatus()) {
            case PENDING:
                break;
            case RUNNING:
                mListener.showProgress();
                break;
            case FINISHED:
                mListener.hideProgress();
                mListener.setResult(mResult);
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.showProgress();
    }

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
            notes.add(new Note("Name " + i, "Text " + i, new Date()));
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
        mListener.hideProgress();
        mResult = notes;
        mListener.setResult(mResult);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mListener.hideProgress();
    }
}
