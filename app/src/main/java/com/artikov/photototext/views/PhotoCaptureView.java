package com.artikov.photototext.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.data.OcrProgress;

/**
 * Date: 4/7/2016
 * Time: 16:51
 *
 * @author Artur Artikov
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PhotoCaptureView extends MvpView {
    void showProgress();

    void hideProgress();

    void setProgress(OcrProgress progress);

    @StateStrategyType(SkipStrategy.class)
    void showNote(Note note);

    @StateStrategyType(SkipStrategy.class)
    void showError(String error);
}
