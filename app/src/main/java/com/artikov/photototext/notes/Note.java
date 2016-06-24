package com.artikov.photototext.notes;

import java.io.Serializable;
import java.util.Date;

/**
 * Date: 23/6/2016
 * Time: 20:52
 *
 * @author Artur Artikov
 */
public class Note implements Serializable {
    private String mName;
    private String mText;
    private Date mDate;

    public Note(String name, String text, Date date) {
        mName = name;
        mText = text;
        mDate = date;
    }

    public String getName() {
        return mName;
    }

    public String getText() {
        return mText;
    }

    public Date getDate() {
        return mDate;
    }
}
