package com.artikov.photototext.presenters;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.artikov.photototext.PhotoToTextApplication;
import com.artikov.photototext.async.NoteAsyncTask;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.db.NoteDataSource;
import com.artikov.photototext.views.NoteListView;

import java.util.List;

/**
 * Date: 4/7/2016
 * Time: 19:48
 *
 * @author Artur Artikov
 */

@InjectViewState
public class NoteListPresenter extends MvpPresenter<NoteListView> implements NoteAsyncTask.Listener {
    private Context mContext;
    private NoteAsyncTask mNoteAsyncTask;

    public NoteListPresenter() {
        mContext = PhotoToTextApplication.getInstance();
    }

    public void loadAll() {
        cancel();
        mNoteAsyncTask = new NoteAsyncTask(mContext, this);
        mNoteAsyncTask.execute();
        getViewState().showProgress();
    }

    public void search(String searchString) {
        cancel();
        mNoteAsyncTask = new NoteAsyncTask(mContext, this);
        mNoteAsyncTask.execute(searchString);
        getViewState().showProgress();
    }

    public void cancel() {
        if (mNoteAsyncTask != null) {
            mNoteAsyncTask.cancel(true);
            mNoteAsyncTask = null;
            getViewState().hideProgress();
        }
    }

    public void deleteNote(Note note) {
        NoteDataSource dataSource = new NoteDataSource(mContext);
        dataSource.delete(note);
        loadAll();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadAll();
    }

    @Override
    public void onLoaded(List<Note> notes) {
        mNoteAsyncTask = null;
        getViewState().hideProgress();
        getViewState().setNotes(notes);
    }
}
