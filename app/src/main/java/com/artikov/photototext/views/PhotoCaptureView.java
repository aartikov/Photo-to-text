package com.artikov.photototext.views;

import com.arellomobile.mvp.MvpView;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.data.OcrProgress;

/**
 * Date: 4/7/2016
 * Time: 16:51
 *
 * @author Artur Artikov
 */
public interface PhotoCaptureView extends MvpView {
    void showProgress();

    void hideProgress();

    void setProgress(OcrProgress progress);

    void showNote(Note note);

    void showError(String error);
}
