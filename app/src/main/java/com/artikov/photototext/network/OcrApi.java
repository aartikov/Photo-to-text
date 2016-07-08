package com.artikov.photototext.network;

import com.artikov.photototext.data.ocr_internal.OcrResponse;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Date: 22/6/2016
 * Time: 23:59
 *
 * @author Artur Artikov
 */
public interface OcrApi {
    @POST("processImage?exportFormat=txt")
    @Headers("Authorization: Basic")
    Observable<OcrResponse> processImage(@Body RequestBody image, @Query("language") String language);

    @GET("getTaskStatus")
    @Headers("Authorization: Basic")
    Observable<OcrResponse> getTaskStatus(@Query("taskId") String taskId);

    @GET
    Observable<String> getResult(@Url String url);
}
