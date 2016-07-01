package com.artikov.photototext.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.artikov.photototext.R;
import com.artikov.photototext.notes.Note;
import com.artikov.photototext.notes.db.NoteDataSource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteActivity extends AppCompatActivity {
    public static final String NOTE_EXTRA = "NOTE";

    @BindView(R.id.activity_note_edit_text_name)
    EditText mNameEditText;

    @BindView(R.id.activity_note_edit_text_text)
    EditText mTextEditText;

    Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);
        mNote = (Note) getIntent().getSerializableExtra(NOTE_EXTRA);
        mNameEditText.setText(mNote.getName());
        mTextEditText.setText(mNote.getText());
    }

    @Override
    public void onBackPressed() {
        if (noteEdited()) {
            updateNote();
            setNoteToResult();
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (noteEdited()) updateNote();
        super.onStop();
    }

    private boolean noteEdited() {
        if (!mNote.getName().equals(mNameEditText.getText().toString())) return true;
        if (!mNote.getText().equals(mTextEditText.getText().toString())) return true;
        return false;
    }

    private void setNoteToResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(NOTE_EXTRA, mNote);
        setResult(RESULT_OK, resultIntent);
    }

    private void updateNote() {
        mNote.setName(mNameEditText.getText().toString());
        mNote.setText(mTextEditText.getText().toString());

        NoteDataSource dataSource = new NoteDataSource(this);
        dataSource.update(mNote);
    }
}
