package com.artikov.photototext.ocr.exceptions;

import com.artikov.photototext.ocr.network.OcrTask;

/**
 * Date: 23/6/2016
 * Time: 22:16
 *
 * @author Artur Artikov
 */
public class InvalidTaskStatusException extends Exception {
    OcrTask.Status mStatus;

    public InvalidTaskStatusException(OcrTask.Status mStatus) {
        super("Invalid Task Status: " + mStatus.toString());
    }

    public OcrTask.Status getStatus() {
        return mStatus;
    }
}
