package com.example.ex11_storageapp.http3;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LocalApiService {

    // @Body : json을 http body에 전송
    // @Path : url pathVariable 에 추가
    // @Query : query string에 추가

//
//    @GET("api/message")
//    Call<List<MessageDto>> getAllMessage();
//
//    @GET("api/message/{id}")
//    Call<MessageDto> getMessage(@Path("id") int id);
//
//    @POST("api/message")
//    Call<String> postMessage(@Body MessageDto dto);
//
//    @PUT("api/message")
//    Call<String> putMessage(@Body MessageDto dto);
//
//    @DELETE("api/message/{id}")
//    Call<String> deleteMessage(@Path("id") int id);
    @GET("/products")
    Call<List<ProductDTO>> getProducts();

    @GET("/product")
    Call<ProductDTO> getProduct(@Query("prodNo") String prodNo);

    @POST("/login")
    @FormUrlEncoded
    Call<Void> login(@Field("id") String id, @Field("pwd") String pwd);

//    @POST("/login")
//    Call<Void> login(@Body Map<String, String> body);
}
