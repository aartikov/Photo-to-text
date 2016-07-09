package com.artikov.photototext.presenters;

import android.content.Context;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.artikov.photototext.PhotoToTextApplication;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.db.NoteDataSource;
import com.artikov.photototext.views.NoteListView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Date: 4/7/2016
 * Time: 19:48
 *
 * @author Artur Artikov
 */

@InjectViewState
public class NoteListPresenter extends MvpPresenter<NoteListView> {
    private static final int QUERY_DEBOUNCE_TIMEOUT = 500;
    private NoteDataSource mNoteDataSource;
    private Subscription mNoteQuerySubscription;
    private PublishSubject<String> mQueryDebounceSubject;
    private String mLastQuery = "";

    public NoteListPresenter() {
        Context context = PhotoToTextApplication.getInstance();
        mNoteDataSource = new NoteDataSource(context);
        mQueryDebounceSubject = PublishSubject.create();
        mQueryDebounceSubject.debounce(QUERY_DEBOUNCE_TIMEOUT, TimeUnit.MILLISECONDS).subscribe(this::queryNotes);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showProgress();
        queryAllNotes();
    }

    public void userEnterQueryText(String query) {
        mQueryDebounceSubject.onNext(query);
    }

    public void userCollapseSearchView() {
        queryAllNotes();
    }

    public void userSwipeOutNote(Note note) {
        mNoteDataSource.delete(note);
        queryNotes(mLastQuery);
    }

    public void userLeaveScreen() {
        cancelQuery();
    }

    public void userReturnToScreen() {
        queryNotes(mLastQuery);
    }

    public String getLastQuery() {
        return mLastQuery;
    }

    private void queryNotes(String query) {
        mLastQuery = query;
        cancelQuery();
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
}
