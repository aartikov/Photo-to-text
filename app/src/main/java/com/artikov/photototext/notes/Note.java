package com.artikov.photototext.notes;

import java.io.Serializable;

/**
 * Date: 23/6/2016
 * Time: 20:52
 *
 * @author Artur Artikov
 */
public class Note implements Serializable {
    private String mText;

    public Note(String text) {
        mText = text;
    }

    public String getText() {
        return mText;
    }
}
