package com.artikov.photototext.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.artikov.photototext.PhotoToTextApplication;
import com.artikov.photototext.R;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.data.OcrInput;
import com.artikov.photototext.data.OcrResult;
import com.artikov.photototext.db.NoteDataSource;
import com.artikov.photototext.network.OcrClient;
import com.artikov.photototext.utils.FileUtils;
import com.artikov.photototext.views.PhotoCaptureView;

import java.util.Date;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Date: 4/7/2016
 * Time: 16:54
 *
 * @author Artur Artikov
 */

@InjectViewState
public class PhotoCapturePresenter extends MvpPresenter<PhotoCaptureView> {
    private static final String MAIN_LANGUAGE_KEY = "main_language";
    private static final String SECONDARY_LANGUAGE_KEY = "secondary_language";

    private Context mContext;
    private NoteDataSource mNoteDataSource;
    private OcrClient mOcrClient;
    private Subscription mOcrSubscription;

    public PhotoCapturePresenter() {
        mContext = PhotoToTextApplication.getInstance();
        PreferenceManager.setDefaultValues(mContext, R.xml.preferences, false);
        mNoteDataSource = new NoteDataSource(mContext);
        mOcrClient = new OcrClient(mContext);
        mOcrClient.getProgressObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progress -> getViewState().setProgress(progress));
    }

    public void userChooseImage(Uri imageUri) {
        recognize(new OcrInput(imageUri, getLanguages()));
    }

    public void userClickCancel() {
        cancelRecognition();
    }

    public void userLeaveScreen() {
        cancelRecognition();
    }

    private void recognize(OcrInput input) {
        cancelRecognition();
        getViewState().showProgress();
        mOcrSubscription = mOcrClient.recognize(input)
                .flatMap(result -> {
                    if (TextUtils.isGraphic(result.getText())) {
                        return Observable.just(result);
                    } else {
                        return Observable.error(new Exception(mContext.getString(R.string.nothing_is_recognized)));
                    }
                })
                .map(this::convertOcrResultToNote)
                .doOnNext(note -> mNoteDataSource.create(note))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Note>() {
                    @Override
                    public void onCompleted() {
                        getViewState().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideProgress();
                        getViewState().showError(getErrorString(e));
                    }

                    @Override
                    public void onNext(Note note) {
                        getViewState().showNote(note);
                    }
                });
    }

    private void cancelRecognition() {
        if (mOcrSubscription != null && !mOcrSubscription.isUnsubscribed()) {
            mOcrSubscription.unsubscribe();
            getViewState().hideProgress();
        }
    }

    private String[] getLanguages() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String[] languages = new String[2];
        languages[0] = preferences.getString(MAIN_LANGUAGE_KEY, "");
        languages[1] = preferences.getString(SECONDARY_LANGUAGE_KEY, "");
        return languages;
    }

    private Note convertOcrResultToNote(OcrResult result) {
        Uri uri = result.getInput().getImageUri();
        String name = FileUtils.getNameByUri(mContext, uri);
        return new Note(name, result.getText(), new Date());
    }

    private String getErrorString(Throwable e) {
        if (e instanceof HttpException) {
            return ((HttpException) e).message();
        } else {
            return e.getLocalizedMessage();
        }
    }
}
