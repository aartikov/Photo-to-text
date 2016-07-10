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
    private String[] mLanguages;

    public OcrInput(Uri imageUri, String[] languages) {
        mImageUri = imageUri;
        mLanguages = languages;
    }
    public Uri getImageUri() {
        return mImageUri;
    }

    public String[] getLanguages() {
        return mLanguages;
    }
}
