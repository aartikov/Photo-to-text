package com.artikov.photototext.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.artikov.photototext.data.Note;

/**
 * Date: 10/7/2016
 * Time: 12:10
 *
 * @author Artur Artikov
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface NoteView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void showNote(Note note);

    void showSaveButton();

    void hideSaveButton();

    void showSaveDialog();

    void hideSaveDialog();

    void close();
}