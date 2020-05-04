package ro.gov.stamacasa.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.Arrays;
import java.util.List;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private static volatile RetrofitService mInstance;
    private RetrofitInterface mInterfaceWs;

    public static RetrofitService getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitService.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitService();
                }
            }
        }
        return mInstance;
    }


    private RetrofitService() {
        List lists = Arrays.asList(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).connectionSpecs(lists).build();
        //.addInterceptor(interceptor).addInterceptor(new OkHttpProfilerInterceptor())

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://95.216.200.50/")
                .build();

        mInterfaceWs = retrofit.create(RetrofitInterface.class);
    }

    public RetrofitInterface getInterface() {
        return mInterfaceWs;
    }
}
