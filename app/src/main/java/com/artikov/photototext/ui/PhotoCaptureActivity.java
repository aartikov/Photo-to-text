package com.artikov.photototext.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.artikov.photototext.R;
import com.artikov.photototext.notes.Note;
import com.artikov.photototext.ocr.OcrClient;
import com.artikov.photototext.ocr.OcrInput;
import com.artikov.photototext.ocr.OcrProgress;
import com.artikov.photototext.ocr.OcrResult;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoCaptureActivity extends AppCompatActivity implements OcrClient.Listener {
    @BindView(R.id.photo_capture_activity_layout_buttons)
    ViewGroup buttonsLayout;

    @BindView(R.id.photo_capture_activity_layout_progress)
    ViewGroup progressLayout;

    @BindView(R.id.photo_capture_activity_text_view_progress)
    TextView progressTextView;

    private OcrClient mOcrClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_capture_activity);
        ButterKnife.bind(this);
        mOcrClient = (OcrClient) getLastCustomNonConfigurationInstance();
        if(mOcrClient == null) mOcrClient = new OcrClient(this);
        else mOcrClient.setListener(this);
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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_item_notes:
                showNoteList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showProgress() {
        buttonsLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        progressTextView.setText("");
    }

    @Override
    public void hideProgress() {
        buttonsLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void updateProgress(OcrProgress progress) {
        switch (progress) {
            case UPLOADING:
                progressTextView.setText(R.string.uploading);
                break;
            case RECOGNITION:
                progressTextView.setText(R.string.recognition);
                break;
            case DOWNLOADING:
                progressTextView.setText(R.string.downloading);
                break;
        }
    }

    @Override
    public void handleResult(OcrResult result) {
        Note note = new Note("OCR", result.getText(), new Date());
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
}
