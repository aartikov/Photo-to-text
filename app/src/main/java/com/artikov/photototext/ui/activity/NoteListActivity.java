package com.artikov.photototext.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.artikov.photototext.R;
import com.artikov.photototext.data.Note;
import com.artikov.photototext.presenters.NoteListPresenter;
import com.artikov.photototext.ui.adapters.NoteAdapter;
import com.artikov.photototext.views.NoteListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteListActivity extends MvpAppCompatActivity implements NoteListView {
    private static final int SHOW_NOTE_REQUEST_CODE = 1;

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

    @InjectPresenter
    NoteListPresenter mPresenter;

    private NoteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    private void initRecyclerView() {
        NoteAdapter.OnItemClickListener onItemClickListener = new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note, int position) {
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
                Note note = mAdapter.getItem(viewHolder.getAdapterPosition());
                mPresenter.deleteNote(note);
            }
        });

        mNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NoteAdapter(this, onItemClickListener);
        mNotesRecyclerView.setAdapter(mAdapter);
        swipeToDismissHelper.attachToRecyclerView(mNotesRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_list_menu, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.note_list_menu_item_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchMenuItem.collapseActionView();
                mPresenter.search(query);
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
        if (requestCode == SHOW_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.loadAll();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.cancel();
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
    public void setNotes(List<Note> notes) {
        mAdapter.setNotes(notes);
        if (mAdapter.getItemCount() == 0) {
            mNotesRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mNotesRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void showNote(Note note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(NoteActivity.NOTE_EXTRA, note);
        startActivityForResult(intent, SHOW_NOTE_REQUEST_CODE);
    }
}
