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
import rx.Subscriber;
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
    private NoteDataSource mNoteDataSource;
    private Subscription mNoteQuerySubscription;

    public NoteListPresenter() {
        Context context = PhotoToTextApplication.getInstance();
        mNoteDataSource = new NoteDataSource(context);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        queryAllNotes();
    }

    public void userEnterQuery(String query) {
        queryNotes(query);
    }

    public void userSwipeOutNote(Note note) {
        deleteNote(note);
    }

    public void userLeaveScreen() {
        cancelQuery();
    }

    public void userReturnToScreen() {
        queryAllNotes();
    }

    private void queryNotes(String query) {
        cancelQuery();
        getViewState().showProgress();
        mNoteQuerySubscription = Observable.fromCallable(() -> mNoteDataSource.queryNotes(query))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Note>>() {
                    @Override
                    public void onCompleted() {
                        getViewState().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Note> notes) {
                        getViewState().setNotes(notes);
                    }
                });
    }

    private void queryAllNotes() {
        queryNotes("");
    }

    private void cancelQuery() {
        if (mNoteQuerySubscription != null && !mNoteQuerySubscription.isUnsubscribed()) {
            mNoteQuerySubscription.unsubscribe();
            getViewState().hideProgress();
        }
    }

    private void deleteNote(Note note) {
        mNoteDataSource.delete(note);
        queryAllNotes();
    }
}
