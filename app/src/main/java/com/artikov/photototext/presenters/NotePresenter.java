package com.artikov.photototext.presenters;

import android.content.Context;
import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.artikov.photototext.PhotoToTextApplication;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.db.NoteDataSource;
import com.artikov.photototext.views.NoteView;

/**
 * Date: 10/7/2016
 * Time: 12:10
 *
 * @author Artur Artikov
 */

@InjectViewState
public class NotePresenter extends MvpPresenter<NoteView> {
    private NoteDataSource mNoteDataSource;
    private Note mNote;
    private String mNoteName;
    private String mNoteText;

    public NotePresenter() {
        Context context = PhotoToTextApplication.getInstance();
        mNoteDataSource = new NoteDataSource(context);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showNote(mNote);
    }

    public void initialize(Note note) {
        mNote = note;
        mNoteName = mNote.getName();
        mNoteText = mNote.getText();
    }

    public boolean isInitialized() {
        return mNote != null;
    }

    public void userEditNoteName(String name) {
        mNoteName = name;
        updateSaveButtonVisibility();
    }

    public void userEditNoteText(String text) {
        mNoteText = text;
        updateSaveButtonVisibility();
    }

    public void userClickSaveButton() {
        saveNote();
        getViewState().hideSaveButton();
    }

    public void userTryToLeaveScreen() {
        if(isNoteChanged()) {
            getViewState().showSaveDialog();
        } else {
            getViewState().close();
        }
    }

    public void userConfirmSave() {
        saveNote();
        getViewState().hideSaveDialog();
        getViewState().close();
    }

    public void userDeclineSave() {
        getViewState().hideSaveDialog();
        getViewState().close();
    }

    public void userCancelSave() {
        getViewState().hideSaveDialog();
    }

    private void updateSaveButtonVisibility() {
        if(isNoteChanged()) {
            getViewState().showSaveButton();
        } else {
            getViewState().hideSaveButton();
        }
    }

    private boolean isNoteChanged() {
        return !TextUtils.equals(mNoteName, mNote.getName()) || !TextUtils.equals(mNoteText, mNote.getText());
    }

    private void saveNote() {
        mNote.setName(mNoteName);
        mNote.setText(mNoteText);
        mNoteDataSource.update(mNote);
    }
}
