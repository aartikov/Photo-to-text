package com.artikov.photototext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.artikov.photototext.ocr.async.OcrAsyncTask;
import com.artikov.photototext.ocr.async.OcrInput;
import com.artikov.photototext.ocr.async.OcrProgress;
import com.artikov.photototext.ocr.async.OcrResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoCaptureActivity extends AppCompatActivity implements OcrAsyncTask.Listener {
    @BindView(R.id.photo_capture_activity_layout_buttons)
    ViewGroup buttonsLayout;

    @BindView(R.id.photo_capture_activity_layout_progress)
    ViewGroup progressLayout;

    @BindView(R.id.photo_capture_activity_text_view_progress)
    TextView progressTextView;

    private OcrAsyncTask mOcrAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_capture_activity);
        ButterKnife.bind(this);
        mOcrAsyncTask = (OcrAsyncTask) getLastCustomNonConfigurationInstance();
        if(mOcrAsyncTask != null) mOcrAsyncTask.setListener(this);
    }

    @OnClick(R.id.photo_capture_activity_button_choose_in_gallery)
    void chooseInGallery() {
        OcrInput input = new OcrInput("/sdcard/Download/Picture_samples/English/Mobile_Photos/MobPhoto_5.jpg");
        startOcr(input);
    }

    @OnClick(R.id.photo_capture_activity_button_take_photo)
    void takePhoto() {
        OcrInput input = new OcrInput("/sdcard/Download/Picture_samples/English/Mobile_Photos/MobPhoto_5.jpg");
        startOcr(input);
    }

    private void startOcr(OcrInput input) {
        cancelOcr();
        mOcrAsyncTask = new OcrAsyncTask(this);
        mOcrAsyncTask.execute(input);
    }

    @OnClick(R.id.photo_capture_activity_button_cancel)
    void cancelOcr() {
        if (mOcrAsyncTask == null) return;
        mOcrAsyncTask.cancel(true);
        mOcrAsyncTask = null;
        hideProgress();
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mOcrAsyncTask;
    }

    @Override
    public void showProgress() {
        buttonsLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        buttonsLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        progressTextView.setText("");
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
        Toast.makeText(this, result.getText(), Toast.LENGTH_SHORT).show();
        mOcrAsyncTask = null;
    }
}
