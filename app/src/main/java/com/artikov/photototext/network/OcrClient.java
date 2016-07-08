package com.artikov.photototext.network;

import android.content.Context;
import android.net.Uri;

import com.artikov.photototext.data.OcrInput;
import com.artikov.photototext.data.OcrProgress;
import com.artikov.photototext.data.OcrResult;
import com.artikov.photototext.data.ocr_internal.OcrResponse;
import com.artikov.photototext.data.ocr_internal.OcrTask;
import com.artikov.photototext.network.exceptions.InvalidTaskStatusException;
import com.artikov.photototext.utils.FileUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Date: 4/7/2016
 * Time: 14:44
 *
 * @author Artur Artikov
 */
public class OcrClient {
    private static final int TASK_STATUS_CHECKING_DELAY = 2000;
    private Context mContext;
    private OcrApi mOcrService;
    private PublishSubject<OcrProgress> mProgressSubject;

    public OcrClient(Context context) {
        mContext = context;
        mOcrService = ServiceGenerator.getInstance().createService(OcrApi.class);
        mProgressSubject = PublishSubject.create();
    }

    public Observable<OcrResult> recognize(OcrInput input) {
        mProgressSubject.onNext(OcrProgress.UPLOADING);
        return processImage(input)
                .doOnNext(ignored -> mProgressSubject.onNext(OcrProgress.RECOGNITION))
                .flatMap(task -> getResultUrl(task.getId()))
                .doOnNext(ignored -> mProgressSubject.onNext(OcrProgress.DOWNLOADING))
                .flatMap(url -> getResult(input, url));
    }

    public Observable<OcrProgress> getProgressObservable() {
        return mProgressSubject;
    }

    private Observable<OcrTask> processImage(OcrInput input) {
        return readImage(input.getImageUri())
                .flatMap(image -> mOcrService.processImage(image, input.getLanguage()))
                .map(OcrResponse::getTask);
    }

    private Observable<String> getResultUrl(String taskId) {
        return mOcrService.getTaskStatus(taskId)
                .delay(TASK_STATUS_CHECKING_DELAY, TimeUnit.MILLISECONDS)
                .repeat()
                .map(OcrResponse::getTask)
                .flatMap(task -> task.isInvalid() ? Observable.error(new InvalidTaskStatusException(task.getStatus())) : Observable.just(task))
                .takeFirst(OcrTask::isCompleted)
                .map(OcrTask::getResultUrl);
    }

    private Observable<OcrResult> getResult(OcrInput input, String resultUrl) {
        return mOcrService.getResult(resultUrl)
                .map(text -> new OcrResult(input, text));
    }

    private Observable<RequestBody> readImage(Uri imageUri) {
        return Observable.fromCallable(() -> {
            byte[] imageData = FileUtils.readFile(mContext, imageUri);
            MediaType mediaType = MediaType.parse("application/octet-stream");
            return RequestBody.create(mediaType, imageData);
        });
    }
}
