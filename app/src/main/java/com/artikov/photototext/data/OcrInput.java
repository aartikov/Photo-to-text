package com.artikov.photototext.data;

import android.net.Uri;

/**
 * Date: 22/6/2016
 * Time: 16:41
 *
 * @author Artur Artikov
 */
public class OcrInput {
    private Uri mImageUri;
    private String mLanguage;

    public OcrInput(Uri imageUri, String language) {
        mImageUri = imageUri;
        mLanguage = language;
    }
    public Uri getImageUri() {
        return mImageUri;
    }

    public String getLanguage() {
        return mLanguage;
    }
}
