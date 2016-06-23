package com.artikov.photototext.ocr.network;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Date: 23/6/2016
 * Time: 14:03
 *
 * @author Artur Artikov
 */
@Root(name = "response", strict = false)
public class OcrResponse {
    @Element(name = "task")
    private OcrTask mTask;

    public OcrTask getTask() {
        return mTask;
    }
}
