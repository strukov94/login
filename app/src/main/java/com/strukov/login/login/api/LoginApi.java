package com.strukov.login.login.api;

import com.strukov.login.login.data.Users;
import com.strukov.login.login.models.AccessToken;
import com.strukov.login.login.models.LoginUser;
import com.strukov.login.login.models.Token;

import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginApi {
    @GET("token/refresh")
    Maybe<Response<AccessToken>> refreshAccessToken(@Header("Authorization") String refreshToken);

    @GET("token/user/{userId}")
    Maybe<Token> getUserToken(@Path("userId") String userId);

    @Headers("Content-Type: application/json")
    @POST("users/new")
    Maybe<LoginUser> addUser(@Body Users user);
}
