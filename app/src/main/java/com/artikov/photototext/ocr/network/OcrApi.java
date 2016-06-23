package com.artikov.photototext.ocr.network;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Date: 22/6/2016
 * Time: 23:59
 *
 * @author Artur Artikov
 */
public interface OcrApi {
    @POST("processImage")
    Call<OcrResponse> processImage(@Body RequestBody image);
}
