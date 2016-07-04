package com.artikov.photototext.views;

import com.arellomobile.mvp.MvpView;
import com.artikov.photototext.data.Note;

import java.util.List;

/**
 * Date: 4/7/2016
 * Time: 19:43
 *
 * @author Artur Artikov
 */
public interface NoteListView extends MvpView {
    void showProgress();

    void hideProgress();

    void setNotes(List<Note> notes);
}
