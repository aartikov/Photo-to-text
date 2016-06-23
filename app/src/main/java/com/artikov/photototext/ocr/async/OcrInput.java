package com.artikov.photototext.ocr.async;

/**
 * Date: 22/6/2016
 * Time: 16:41
 *
 * @author Artur Artikov
 */
public class OcrInput {
    private String mImageFilePath;
    private String mLanguage;

    public OcrInput(String imageFilePath, String language) {
        mImageFilePath = imageFilePath;
        mLanguage = language;
    }
    public String getImageFilePath() {
        return mImageFilePath;
    }

    public String getLanguage() {
        return mLanguage;
    }
}
