package com.strukov.login.login.login;

import com.strukov.login.login.data.Users;
import com.strukov.login.login.models.LoginUser;
import com.strukov.login.login.models.Token;
import com.strukov.login.login.models.UserResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Response;

public interface LoginModel {
    Maybe<Token> getUserToken(String userId);

    Maybe<Response<UserResponse>> getUserInfo(String userId, String token, String version);

    Maybe<LoginUser> addUser(Users users);

    Single<Long> insertUser(Users user);

    Single<Integer> updateUser(Users user);

    Single<List<Integer>> userExist(String token);

    Completable setToken(String token);

    Completable setUserId(int userId);

    Completable setJWT(String accessToken, String refreshToken);
}
