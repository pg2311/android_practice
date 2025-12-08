package com.example.ex11_storageapp.http2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // @Body : json을 http body에 전송
    // @Path : url pathVariable 에 추가
    // @Query : query string에 추가


    @GET("posts/{id}")
    Call<Post> getData(@Path("id") String id);

    @GET("posts")
    Call<List<Post>> getAllData();

    @POST("posts")
    Call<Post> insertPost(@Body Post dto);

}
