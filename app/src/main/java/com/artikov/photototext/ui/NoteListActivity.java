package com.artikov.photototext.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.artikov.photototext.R;
import com.artikov.photototext.notes.Note;
import com.artikov.photototext.notes.NoteAdapter;
import com.artikov.photototext.notes.NoteLoader;
import com.artikov.photototext.notes.db.NoteDataSource;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteListActivity extends AppCompatActivity implements NoteLoader.Listener {
    private static final int SHOW_NOTE_REQUEST_CODE = 1;
    private static final java.lang.String SELECTED_POSITION_TAG = "SELECTED_POSITION";

    @BindView(R.id.activity_note_list_recycler_view_notes)
    RecyclerView mNotesRecyclerView;

    @BindView(R.id.activity_note_list_layout_list)
    ViewGroup mListLayout;

    @BindView(R.id.activity_note_list_layout_progress)
    ViewGroup mProgressLayout;

    @BindView(R.id.activity_note_list_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.activity_note_list_text_view_empty_view)
    View mEmptyView;

    private NoteAdapter mAdapter;
    private NoteLoader mNoteLoader;
    private int mSelectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);
        initRecyclerView();

        mNoteLoader = (NoteLoader) getLastCustomNonConfigurationInstance();
        if (mNoteLoader == null) {
            mNoteLoader = new NoteLoader(getApplicationContext(), this);
            mNoteLoader.load("");
        } else {
            mNoteLoader.setListener(this);
        }
    }

    private void initRecyclerView() {
        NoteAdapter.OnItemClickListener onItemClickListener = new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note, int position) {
                mSelectedPosition = position;
                showNote(note);
            }
        };

        ItemTouchHelper swipeToDismissHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteNote(viewHolder.getAdapterPosition());
            }
        });

        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NoteAdapter(this, onItemClickListener);
        mNotesRecyclerView.setAdapter(mAdapter);
        swipeToDismissHelper.attachToRecyclerView(mNotesRecyclerView);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mNoteLoader;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_POSITION_TAG, mSelectedPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSelectedPosition = savedInstanceState.getInt(SELECTED_POSITION_TAG, -1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_list_menu, menu);
        final MenuItem searchMenuItem = menu.findItem( R.id.note_list_menu_item_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMenuItem.collapseActionView();
                mNoteLoader.load(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHOW_NOTE_REQUEST_CODE && resultCode == RESULT_OK && data != null && mSelectedPosition != -1) {
            Note note = (Note) data.getSerializableExtra(NoteActivity.NOTE_EXTRA);
            mAdapter.setItem(mSelectedPosition, note);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mNoteLoader.cancel();
    }

    @Override
    public void showProgress() {
        mListLayout.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mListLayout.setVisibility(View.VISIBLE);
        mProgressLayout.setVisibility(View.GONE);
    }

    @Override
    public void setResult(List<Note> notes) {
        mAdapter.setNotes(notes);
        checkEmptyState();
    }

    private void showNote(Note note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(NoteActivity.NOTE_EXTRA, note);
        startActivityForResult(intent, SHOW_NOTE_REQUEST_CODE);
    }

    private void deleteNote(int position) {
        NoteDataSource dataSource = new NoteDataSource(NoteListActivity.this);
        dataSource.delete(mAdapter.getItem(position));
        mAdapter.removeItem(position);
        checkEmptyState();
    }

    private void checkEmptyState() {
        if (mAdapter.getItemCount() == 0) {
            mNotesRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mNotesRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }
}
