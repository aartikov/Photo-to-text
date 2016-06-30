package com.artikov.photototext.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.artikov.photototext.R;
import com.artikov.photototext.notes.Note;
import com.artikov.photototext.notes.db.NoteDataSource;
import com.artikov.photototext.ocr.OcrClient;
import com.artikov.photototext.ocr.OcrInput;
import com.artikov.photototext.ocr.OcrProgress;
import com.artikov.photototext.ocr.OcrResult;
import com.artikov.photototext.utils.FileNameUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoCaptureActivity extends AppCompatActivity implements OcrClient.Listener, NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.photo_capture_activity_layout_buttons)
    ViewGroup mButtonsLayout;

    @BindView(R.id.photo_capture_activity_layout_progress)
    ViewGroup mProgressLayout;

    @BindView(R.id.photo_capture_activity_text_view_progress)
    TextView mProgressTextView;

    @BindView(R.id.photo_capture_activity_drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.photo_capture_activity_navigation_view)
    NavigationView mNavigationView;

    private ActionBarDrawerToggle mDrawerToggle;

    private OcrClient mOcrClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_capture_activity);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        mOcrClient = (OcrClient) getLastCustomNonConfigurationInstance();
        if (mOcrClient == null) {
            mOcrClient = new OcrClient(this);
        } else {
            mOcrClient.setListener(this);
        }
    }

    @OnClick(R.id.photo_capture_activity_button_choose_in_gallery)
    void chooseInGallery() {
        OcrInput input = new OcrInput("/sdcard/Download/Picture_samples/English/Mobile_Photos/IMG_0122.jpg", "English");
        mOcrClient.recognize(input);
    }

    @OnClick(R.id.photo_capture_activity_button_take_photo)
    void takePhoto() {
        OcrInput input = new OcrInput("/sdcard/Download/Picture_samples/Russian/[Untitled]001.jpg", "Russian");
        mOcrClient.recognize(input);
    }

    @OnClick(R.id.photo_capture_activity_button_cancel)
    void cancelOcr() {
        mOcrClient.cancel();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mOcrClient;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.main_menu_item_notes:
                showNoteList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_menu_one:
                Snackbar.make(mDrawerLayout, R.string.one, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.navigation_menu_two:
                Snackbar.make(mDrawerLayout, R.string.two, Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.navigation_menu_three:
                Snackbar.make(mDrawerLayout, R.string.three, Snackbar.LENGTH_SHORT).show();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showProgress() {
        mButtonsLayout.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.VISIBLE);
        mProgressTextView.setText("");
    }

    @Override
    public void hideProgress() {
        mButtonsLayout.setVisibility(View.VISIBLE);
        mProgressLayout.setVisibility(View.GONE);
    }

    @Override
    public void updateProgress(OcrProgress progress) {
        switch (progress) {
            case UPLOADING:
                mProgressTextView.setText(R.string.uploading);
                break;
            case RECOGNITION:
                mProgressTextView.setText(R.string.recognition);
                break;
            case DOWNLOADING:
                mProgressTextView.setText(R.string.downloading);
                break;
        }
    }

    @Override
    public void handleResult(OcrResult result) {
        String path = result.getInput().getImageFilePath();
        String name = FileNameUtils.getFileNameWithoutExtension(path);
        Note note = new Note(name, result.getText(), new Date());
        addNoteToDatabase(note);
        showNote(note);
    }

    @Override
    public void handleException(Exception exception) {
        Toast.makeText(this, exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    private void showNote(Note note) {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra(NoteActivity.NOTE_EXTRA, note);
        startActivity(intent);
    }

    private void showNoteList() {
        cancelOcr();
        Intent intent = new Intent(this, NoteListActivity.class);
        startActivity(intent);
    }

    private void addNoteToDatabase(Note note) {
        NoteDataSource dataSource = new NoteDataSource(this);
        dataSource.create(note);
    }
}
