package com.artikov.photototext.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.artikov.photototext.R;
import com.artikov.photototext.notes.Note;
import com.artikov.photototext.notes.NoteAdapter;
import com.artikov.photototext.notes.NoteAsyncTask;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteListActivity extends AppCompatActivity implements NoteAsyncTask.Listener {
    @BindView(R.id.note_list_activity_recycler_view_notes)
    RecyclerView mNotesRecyclerView;

    @BindView(R.id.note_list_activity_layout_progress)
    ViewGroup mProgressLayout;

    @BindView(R.id.note_list_activity_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.note_list_activity_text_view_progress)
    TextView mProgressTextView;

    private NoteAdapter mAdapter;
    private NoteAsyncTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list_activity);
        ButterKnife.bind(this);

        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NoteAdapter(this);
        mNotesRecyclerView.setAdapter(mAdapter);

        mAsyncTask = (NoteAsyncTask) getLastCustomNonConfigurationInstance();
        if (mAsyncTask == null) {
            mAsyncTask = new NoteAsyncTask(this);
            mAsyncTask.execute();
        } else {
            mAsyncTask.setListener(this);
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mAsyncTask;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAsyncTask.cancel(true);
    }

    @Override
    public void showProgress() {
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressTextView.setText(getString(R.string.percent_template, 0));
    }

    @Override
    public void updateProgress(int progress) {
        mProgressTextView.setText(getString(R.string.percent_template, progress));
    }

    @Override
    public void hideProgress() {
        mProgressLayout.setVisibility(View.GONE);
    }

    @Override
    public void setResult(List<Note> notes) {
        mAdapter.setNotes(notes);
    }
}
