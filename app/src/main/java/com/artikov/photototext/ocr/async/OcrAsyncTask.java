package com.artikov.photototext.ocr.async;

import android.os.AsyncTask;

import com.artikov.photototext.ocr.network.OcrApi;
import com.artikov.photototext.ocr.network.OcrResponse;
import com.artikov.photototext.ocr.network.OcrTask;
import com.artikov.photototext.ocr.network.ServiceGenerator;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Date: 22/6/2016
 * Time: 16:00
 *
 * @author Artur Artikov
 */
public class OcrAsyncTask extends AsyncTask<OcrInput, OcrProgress, OcrResult> {
    private Listener mListener;

    public interface Listener {
        void showProgress();

        void hideProgress();

        void updateProgress(OcrProgress progress);

        void handleResult(OcrResult mResult);
    }

    public OcrAsyncTask(Listener listener) {
        mListener = listener;
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
            OcrApi ocrService = ServiceGenerator.getInstance().createService(OcrApi.class);

            publishProgress(OcrProgress.UPLOADING);
            File imageFile = new File(input.getImageFilePath());
            RequestBody image = RequestBody.create(MediaType.parse("application/octet-stream"), imageFile);
            Response<OcrResponse> processImageResponse = ocrService.processImage(image).execute();
            if (!processImageResponse.isSuccessful()) {
                OcrTask task = processImageResponse.body().getTask();
                return new OcrResult(task.getId());
            } else {
                return new OcrResult("Error");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new OcrResult("Error");
        }
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
        mListener.handleResult(result);
    }
}
