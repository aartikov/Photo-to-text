package com.artikov.photototext.ocr;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

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
        if(getStatus() == Status.RUNNING) mListener.showProgress();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.showProgress();
    }

    @Override
    protected OcrResult doInBackground(OcrInput... inputs) {
        try {
            publishProgress(OcrProgress.UPLOADING);
            TimeUnit.MILLISECONDS.sleep(2000);
            publishProgress(OcrProgress.RECOGNITION);
            TimeUnit.MILLISECONDS.sleep(2000);
            publishProgress(OcrProgress.DOWNLOADING);
            TimeUnit.MILLISECONDS.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new OcrResult("");
        }
        return new OcrResult("Result!!!");
    };

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
