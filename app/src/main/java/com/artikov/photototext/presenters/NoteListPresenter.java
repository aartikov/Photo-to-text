package com.artikov.photototext.presenters;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.artikov.photototext.PhotoToTextApplication;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.db.NoteDataSource;
import com.artikov.photototext.views.NoteListView;

import java.util.List;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Date: 4/7/2016
 * Time: 19:48
 *
 * @author Artur Artikov
 */

@InjectViewState
public class NoteListPresenter extends MvpPresenter<NoteListView> {
    private Context mContext;
    private Subscription mNoteQuerySubscription;

    public NoteListPresenter() {
        mContext = PhotoToTextApplication.getInstance();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        queryAllNotes();
    }

    public void queryAllNotes() {
        queryNotes("");
    }

    public void queryNotes(String query) {
        cancelQuery();
        getViewState().showProgress();
        mNoteQuerySubscription = Observable.create((OnSubscribe<List<Note>>) subscriber -> {
            NoteDataSource dataSource = new NoteDataSource(mContext);
            subscriber.onNext(dataSource.queryNotes(query));
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(notes -> {
                    getViewState().hideProgress();
                    getViewState().setNotes(notes);
                });
    }

    public void cancelQuery() {
        if (mNoteQuerySubscription != null) {
            mNoteQuerySubscription.unsubscribe();
            mNoteQuerySubscription = null;
            getViewState().hideProgress();
        }
    }

    public void deleteNote(Note note) {
        NoteDataSource dataSource = new NoteDataSource(mContext);
        dataSource.delete(note);
        queryAllNotes();
    }
}
