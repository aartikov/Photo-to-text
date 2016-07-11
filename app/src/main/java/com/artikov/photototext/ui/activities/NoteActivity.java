package com.artikov.photototext.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.artikov.photototext.R;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.presenters.NotePresenter;
import com.artikov.photototext.views.NoteView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteActivity extends MvpAppCompatActivity implements NoteView {
    public static final String NOTE_EXTRA = "NOTE";

    @BindView(R.id.activity_note_edit_text_name)
    EditText mNameEditText;

    @BindView(R.id.activity_note_edit_text_text)
    EditText mTextEditText;

    private AlertDialog mSaveDialog;

    private boolean mSaveButtonVisible = false;

    @InjectPresenter
    NotePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.userEditNoteName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mTextEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.userEditNoteText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (!mPresenter.isInitialized()) {
            Note note = (Note) getIntent().getSerializableExtra(NOTE_EXTRA);
            mPresenter.initialize(note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        MenuItem saveMenuItem = menu.findItem(R.id.note_menu_item_save);
        saveMenuItem.setVisible(mSaveButtonVisible);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mPresenter.userTryToLeaveScreen();
                return true;
            case R.id.note_menu_item_save:
                mPresenter.userClickSaveButton();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissSaveDialog();
    }

    @Override
    public void showNote(Note note) {
        mNameEditText.setText(note.getName());
        mTextEditText.setText(note.getText());
    }

    @Override
    public void showSaveButton() {
        if (mSaveButtonVisible) return;
        mSaveButtonVisible = true;
        invalidateOptionsMenu();
    }

    @Override
    public void hideSaveButton() {
        if (!mSaveButtonVisible) return;
        mSaveButtonVisible = false;
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        mPresenter.userTryToLeaveScreen();
    }

    @Override
    public void showSaveDialog() {
        dismissSaveDialog();
        mSaveDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.save_note))
                .setMessage(R.string.note_save_confirmation)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> mPresenter.userConfirmSave())
                .setNegativeButton(R.string.no, (dialogInterface, i) -> mPresenter.userDeclineSave())
                .setNeutralButton(R.string.cancel, (dialogInterface, i) -> mPresenter.userCancelSave())
                .setOnCancelListener(dialogInterface -> mPresenter.userCancelSave())
                .show();
    }

    @Override
    public void hideSaveDialog() {
        dismissSaveDialog();
    }

    @Override
    public void close() {
        finish();
    }

    private void dismissSaveDialog() {
        if (mSaveDialog != null) {
            mSaveDialog.dismiss();
        }
    }
}
