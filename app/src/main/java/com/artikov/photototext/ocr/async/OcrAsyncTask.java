package com.artikov.photototext.ocr.async;

import android.os.AsyncTask;

import com.artikov.photototext.ocr.exceptions.InvalidResponseException;
import com.artikov.photototext.ocr.exceptions.InvalidTaskStatusException;
import com.artikov.photototext.ocr.network.OcrApi;
import com.artikov.photototext.ocr.network.OcrResponse;
import com.artikov.photototext.ocr.network.OcrTask;
import com.artikov.photototext.ocr.network.ServiceGenerator;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Date: 22/6/2016
 * Time: 16:00
 *
 * @author Artur Artikov
 */
public class OcrAsyncTask extends AsyncTask<OcrInput, OcrProgress, OcrResult> {
    private static final int TASK_STATUS_CHECKING_DELAY = 2000;
    private Listener mListener;
    private OcrApi mOcrService;
    private Exception mException;

    public interface Listener {
        void showProgress();

        void hideProgress();

        void updateProgress(OcrProgress progress);

        void handleResult(OcrResult result);

        void handleException(Exception exception);
    }

    public OcrAsyncTask(Listener listener) {
        mListener = listener;
        mOcrService = ServiceGenerator.getInstance().createService(OcrApi.class);
    }

    public void setListener(Listener listener) {
        mListener = listener;
        if (getStatus() == Status.RUNNING) mListener.showProgress();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.showProgress();
    }

    @Override
    protected OcrResult doInBackground(OcrInput... inputs) {
        OcrInput input = inputs[0];
        try {
            publishProgress(OcrProgress.UPLOADING);
            OcrTask task = processImage(input);

            publishProgress(OcrProgress.RECOGNITION);
            while (!task.isCompleted() && !task.isInvalid()) {
                Thread.sleep(TASK_STATUS_CHECKING_DELAY);
                task = getTaskStatus(task);
            }
            if (task.isInvalid()) throw new InvalidTaskStatusException(task.getStatus());

            publishProgress(OcrProgress.DOWNLOADING);
            return getResult(task);
        } catch (IOException | InvalidResponseException | InvalidTaskStatusException e) {
            e.printStackTrace();
            mException = e;
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private OcrTask processImage(OcrInput input) throws IOException, InvalidResponseException {
        String language = input.getLanguage();
        File imageFile = new File(input.getImageFilePath());
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody image = RequestBody.create(mediaType, imageFile);

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

    private OcrResult getResult(OcrTask task) throws IOException, InvalidResponseException {
        Response<ResponseBody> response = mOcrService.getResult(task.getResultUrl()).execute();
        if (!response.isSuccessful()) {
            throw new InvalidResponseException("Failed to download result");
        }
        String text = response.body().string();
        return new OcrResult(text);
    }

    @Override
    protected void onProgressUpdate(OcrProgress... progresses) {
        super.onProgressUpdate(progresses);
        mListener.updateProgress(progresses[0]);
    }

    @Override
    protected void onPostExecute(OcrResult result) {
        super.onPostExecute(result);
        mListener.hideProgress();
        if (mException != null) mListener.handleException(mException);
        else if (result != null) mListener.handleResult(result);
    }
}
