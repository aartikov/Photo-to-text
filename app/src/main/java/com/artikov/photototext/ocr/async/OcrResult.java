package com.artikov.photototext.ocr.async;

/**
 * Date: 22/6/2016
 * Time: 16:26
 *
 * @author Artur Artikov
 */
public class OcrResult {
    private String mText;

    public OcrResult(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }
}
