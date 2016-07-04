package com.artikov.photototext.network;

import android.content.Context;
import android.net.Uri;

import com.artikov.photototext.data.OcrInput;
import com.artikov.photototext.data.OcrProgress;
import com.artikov.photototext.data.OcrResult;
import com.artikov.photototext.data.ocr_internal.OcrResponse;
import com.artikov.photototext.data.ocr_internal.OcrTask;
import com.artikov.photototext.network.exceptions.InvalidResponseException;
import com.artikov.photototext.network.exceptions.InvalidTaskStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Date: 4/7/2016
 * Time: 14:44
 *
 * @author Artur Artikov
 */
public class OcrClient {
    private static final int TASK_STATUS_CHECKING_DELAY = 2000;
    private Context mContext;
    private ProgressCallback mProgressCallback;
    private OcrApi mOcrService;

    public interface ProgressCallback {
        void onProgress(OcrProgress progress);
    }

    public OcrClient(Context context, ProgressCallback progressCallback) {
        mContext = context;
        mProgressCallback = progressCallback;
        mOcrService = ServiceGenerator.getInstance().createService(OcrApi.class);
    }

    public OcrResult recognize(OcrInput input) throws IOException, InterruptedException, InvalidResponseException, InvalidTaskStatusException {
        mProgressCallback.onProgress(OcrProgress.UPLOADING);
        OcrTask task = processImage(input);

        mProgressCallback.onProgress(OcrProgress.RECOGNITION);
        while (!task.isCompleted() && !task.isInvalid()) {
            TimeUnit.MILLISECONDS.sleep(TASK_STATUS_CHECKING_DELAY);
            task = getTaskStatus(task);
        }
        if (task.isInvalid()) {
            throw new InvalidTaskStatusException(task.getStatus());
        }

        mProgressCallback.onProgress(OcrProgress.DOWNLOADING);
        return getResult(input, task);
    }

    private OcrTask processImage(OcrInput input) throws IOException, InvalidResponseException {
        String language = input.getLanguage();
        byte[] imageData = readImage(input.getImageUri());
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody image = RequestBody.create(mediaType, imageData);

        Response<OcrResponse> response = mOcrService.processImage(image, language).execute();
        if (!response.isSuccessful()) {
            throw new InvalidResponseException(response.message());
        }
        return response.body().getTask();
    }

    private OcrTask getTaskStatus(OcrTask task) throws IOException, InvalidResponseException {
        Response<OcrResponse> response = mOcrService.getTaskStatus(task.getId()).execute();
        if (!response.isSuccessful()) {
            throw new InvalidResponseException(response.message());
        }
        return response.body().getTask();
    }

    private OcrResult getResult(OcrInput input, OcrTask task) throws IOException, InvalidResponseException {
        Response<ResponseBody> response = mOcrService.getResult(task.getResultUrl()).execute();
        if (!response.isSuccessful()) {
            throw new InvalidResponseException("Failed to download result");
        }
        String text = response.body().string();
        return new OcrResult(input, text);
    }

    private byte[] readImage(Uri imageUri) throws IOException {
        InputStream stream = mContext.getContentResolver().openInputStream(imageUri);
        if (stream == null) {
            throw new IOException("Could not read image " + imageUri);
        }

        byte[] data = new byte[stream.available()];
        try {
            int offset = 0;
            while (true) {
                if (offset >= data.length) break;
                int count = stream.read(data, offset, data.length - offset);
                if (count < 0) break;
                offset += count;
            }
            if (offset < data.length) {
                throw new IOException("Could not read image " + imageUri);
            }
        } finally {
            stream.close();
        }
        return data;
    }
}
