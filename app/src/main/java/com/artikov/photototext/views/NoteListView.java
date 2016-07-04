package com.artikov.photototext.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.artikov.photototext.data.Note;

import java.util.List;

/**
 * Date: 4/7/2016
 * Time: 19:43
 *
 * @author Artur Artikov
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface NoteListView extends MvpView {
    void showProgress();

    void hideProgress();

    void setNotes(List<Note> notes);
}
