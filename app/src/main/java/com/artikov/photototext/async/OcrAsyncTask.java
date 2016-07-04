package com.artikov.photototext.async;

import android.content.Context;
import android.os.AsyncTask;

import com.artikov.photototext.data.OcrInput;
import com.artikov.photototext.data.OcrProgress;
import com.artikov.photototext.data.OcrResult;
import com.artikov.photototext.network.OcrClient;
import com.artikov.photototext.network.exceptions.InvalidResponseException;
import com.artikov.photototext.network.exceptions.InvalidTaskStatusException;

import java.io.IOException;

/**
 * Date: 4/7/2016
 * Time: 14:50
 *
 * @author Artur Artikov
 */
public class OcrAsyncTask extends AsyncTask<OcrInput, OcrProgress, OcrResult> {
    private Context mContext;
    private Listener mListener;
    private Exception mException;

    public interface Listener {
        void onProgress(OcrProgress progress);

        void onRecognized(OcrResult result);

        void onError(Exception exception);
    }

    public OcrAsyncTask(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected OcrResult doInBackground(OcrInput... inputs) {
        OcrInput input = inputs[0];
        try {
            OcrClient ocrClient = new OcrClient(mContext, new OcrClient.ProgressCallback() {
                @Override
                public void onProgress(OcrProgress progress) {
                    publishProgress(progress);
                }
            });
            return ocrClient.recognize(input);
        } catch (IOException | InvalidResponseException | InvalidTaskStatusException e) {
            e.printStackTrace();
            mException = e;
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(OcrProgress... progresses) {
        super.onProgressUpdate(progresses);
        mListener.onProgress(progresses[0]);
    }

    @Override
    protected void onPostExecute(OcrResult result) {
        super.onPostExecute(result);
        if (mException != null) {
            mListener.onError(mException);
        } else if (result != null) {
            mListener.onRecognized(result);
        }
    }
}