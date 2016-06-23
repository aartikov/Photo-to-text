package com.artikov.photototext.ocr.async;

/**
 * Date: 22/6/2016
 * Time: 16:41
 *
 * @author Artur Artikov
 */
public class OcrInput {
    private String mImageFilePath;

    public OcrInput(String imageFilePath) {
        mImageFilePath = imageFilePath;
    }

    public String getImageFilePath() {
        return mImageFilePath;
    }
}
