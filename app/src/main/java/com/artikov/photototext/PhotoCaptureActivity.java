package com.artikov.photototext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoCaptureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_capture_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.photo_capture_activity_button_choose_in_gallery)
    void chooseInGallery() {
        Toast.makeText(this, "chooseInGallery", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.photo_capture_activity_button_take_photo)
    void takePhoto() {
        Toast.makeText(this, "takePhoto", Toast.LENGTH_SHORT).show();
    }
}
