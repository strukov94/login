package com.strukov.login.login.api;

import com.strukov.login.login.models.UserResponse;

import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VkApi {
    @GET("users.get")
    Maybe<Response<UserResponse>> getUserInfo(@Query("user_ids") String userId, @Query("access_token") String token, @Query("v") String version);

}
