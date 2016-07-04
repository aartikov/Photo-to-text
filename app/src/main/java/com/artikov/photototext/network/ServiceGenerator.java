package com.artikov.photototext.network;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Date: 22/6/2016
 * Time: 23:49
 *
 * @author Artur Artikov
 */
public class ServiceGenerator {
    private static final String BASE_URL = "http://cloud.ocrsdk.com/";
    private static final String APPLICATION_ID = "Photo to text";
    private static final String PASSWORD = "WVpAfih/LoYIN/CpWk8oGF2U";

    static private ServiceGenerator sInstance;
    static private Retrofit sRetrofit;

    private ServiceGenerator() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor authorizationInterceptor = createAuthorizationInterceptor();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authorizationInterceptor)
                .build();

        sRetrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .baseUrl(BASE_URL).build();
    }

    static public ServiceGenerator getInstance() {
        if (sInstance == null) {
            sInstance = new ServiceGenerator();
        }
        return sInstance;
    }

    public <T> T createService(Class<T> serviceClass) {
        return (T) sRetrofit.create(serviceClass);
    }

    private Interceptor createAuthorizationInterceptor() {
        String credentials = APPLICATION_ID + ":" + PASSWORD;
        final String basicAuthorization = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                if(!"Basic".equals(original.header("Authorization"))) return chain.proceed(original);

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", basicAuthorization);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }
}
