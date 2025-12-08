package com.example.ex11_storageapp;

import android.app.Application;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
1. 전역 상태 및 자원 초기화 (가장 중요한 이유)
2. 컴포넌트 간 데이터 공유 (Activity 간 통신보다 효율적)

Application 상속. 한번만 만들어지는 최상위 객체
android menifest.xml에  선언되어야 합니다!
 <application
 android:name=".ApplicationClass"   이 부분에 정의된 클래스 이름을 지정
* */
public class ApplicationClass extends Application {

    //ends with '/'
    private String API_URL = "https://jsonplaceholder.typicode.com/";

    //ends with '/'
//    private String API_LOCAL_URL = "http://10.10.0.17:9988/";
    private String API_LOCAL_URL = "http://10.10.0.81:8888/";

    public static Retrofit retrofit;
    public static Retrofit localRetrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        //setLenient() : GSon은 엄격한 json type을 요구하는데, 느슨하게 하기 위한 설정. success, fail이 json이 아니라 단순 문자열로 리턴될 경우 처리..
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();

        localRetrofit = new Retrofit.Builder()
                .baseUrl(API_LOCAL_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
    }

}
