package com.artikov.photototext.ocr.exceptions;

/**
 * Date: 23/6/2016
 * Time: 22:16
 *
 * @author Artur Artikov
 */
public class InvalidResponseException extends Exception {
    public InvalidResponseException(String detailMessage) {
        super(detailMessage);
    }
}
