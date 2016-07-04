package com.artikov.photototext.presenters;

import android.content.Context;
import android.net.Uri;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.artikov.photototext.PhotoToTextApplication;
import com.artikov.photototext.async.OcrAsyncTask;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.data.OcrInput;
import com.artikov.photototext.data.OcrProgress;
import com.artikov.photototext.data.OcrResult;
import com.artikov.photototext.db.NoteDataSource;
import com.artikov.photototext.utils.FileNameUtils;
import com.artikov.photototext.views.PhotoCaptureView;

import java.util.Date;

/**
 * Date: 4/7/2016
 * Time: 16:54
 *
 * @author Artur Artikov
 */

@InjectViewState
public class PhotoCapturePresenter extends MvpPresenter<PhotoCaptureView> implements OcrAsyncTask.Listener {
    private Context mContext;
    private OcrAsyncTask mOcrAsyncTask;

    public PhotoCapturePresenter() {
        mContext = PhotoToTextApplication.getInstance();
    }

    public void recognize(OcrInput input) {
        cancel();
        mOcrAsyncTask = new OcrAsyncTask(mContext, this);
        mOcrAsyncTask.execute(input);
        getViewState().showProgress();
    }

    public void cancel() {
        if (mOcrAsyncTask != null) {
            mOcrAsyncTask.cancel(true);
            mOcrAsyncTask = null;
            getViewState().hideProgress();
        }
    }

    @Override
    public void onProgress(OcrProgress progress) {
        getViewState().setProgress(progress);
    }

    @Override
    public void onRecognized(OcrResult result) {
        mOcrAsyncTask = null;
        getViewState().hideProgress();
        Note note = convertOcrResultToNote(result);
        addNoteToDatabase(note);
        getViewState().showNote(note);
    }

    @Override
    public void onError(Exception exception) {
        mOcrAsyncTask = null;
        getViewState().hideProgress();
        getViewState().showError(exception.getLocalizedMessage());
    }

    private Note convertOcrResultToNote(OcrResult result) {
        Uri uri = result.getInput().getImageUri();
        String name = FileNameUtils.getName(mContext, uri);
        return new Note(name, result.getText(), new Date());
    }

    private void addNoteToDatabase(Note note) {
        NoteDataSource dataSource = new NoteDataSource(mContext);
        dataSource.create(note);
    }
}
