package com.artikov.photototext.ocr;

/**
 * Date: 22/6/2016
 * Time: 16:26
 *
 * @author Artur Artikov
 */
public class OcrResult {
    private OcrInput mInput;
    private String mText;

    public OcrResult(OcrInput input, String text) {
        mInput = input;
        mText = text;
    }

    public OcrInput getInput() {
        return mInput;
    }

    public String getText() {
        return mText;
    }
}
