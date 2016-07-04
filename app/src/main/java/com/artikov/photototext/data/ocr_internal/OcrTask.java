package com.artikov.photototext.data.ocr_internal;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Date: 22/6/2016
 * Time: 17:47
 *
 * @author Artur Artikov
 */

@Root(strict = false)
public class OcrTask {
    public enum Status {
        Submitted, Queued, InProgress, Completed, ProcessingFailed, Deleted, NotEnoughCredits
    }

    @Attribute(name = "status")
    private Status mStatus;

    @Attribute(name = "id")
    public String mId;

    @Attribute(name = "resultUrl", required = false)
    private String mResultUrl;

    public Status getStatus() {
        return mStatus;
    }

    public String getId() {
        return mId;
    }

    public String getResultUrl() {
        return mResultUrl;
    }

    public boolean isRunning() {
        return mStatus == Status.Submitted || mStatus == Status.Queued || mStatus == Status.InProgress;
    }

    public boolean isCompleted() {
        return mStatus == Status.Completed;
    }

    public boolean isInvalid() {
        return mStatus == Status.ProcessingFailed || mStatus == Status.Deleted || mStatus == Status.NotEnoughCredits;
    }
}
