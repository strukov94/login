package com.strukov.login.login.login;

import android.content.SharedPreferences;

import com.strukov.login.login.api.LoginApi;
import com.strukov.login.login.api.VkApi;
import com.strukov.login.login.data.LoginDao;
import com.strukov.login.login.data.Users;
import com.strukov.login.login.enums.LoginPreference;
import com.strukov.login.login.models.LoginUser;
import com.strukov.login.login.models.Token;
import com.strukov.login.login.models.UserResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Response;

public class LoginModelImpl implements LoginModel {

    private LoginApi mApi;
    private VkApi mVkApi;
    private LoginDao mDao;
    private SharedPreferences mPreference;

    public LoginModelImpl(LoginApi api, VkApi vkApi, LoginDao dao, SharedPreferences preferences) {
        mApi = api;
        mVkApi = vkApi;
        mDao = dao;
        mPreference = preferences;
    }

    @Override
    public Maybe<Token> getUserToken(String userId) {
        return mApi.getUserToken(userId);
    }

    @Override
    public Maybe<Response<UserResponse>> getUserInfo(String userId, String token, String version) {
        return mVkApi.getUserInfo(userId, token, version);
    }

    @Override
    public Single<List<Integer>> userExist(String token) {
        return mDao.userExist(token);
    }

    @Override
    public Single<Long> insertUser(Users user) {
        return mDao.insertUser(user);
    }

    @Override
    public Single<Integer> updateUser(Users user) {
        return mDao.updateUser(user);
    }

    @Override
    public Completable setToken(String token) {
        return Completable.fromAction(() ->
                mPreference.edit().putString(LoginPreference.PREF_TOKEN.name(), token).apply());
    }

    @Override
    public Completable setUserId(int userId) {
        return Completable.fromAction(() ->
                mPreference.edit().putInt(LoginPreference.PREF_USER_ID.name(), userId).apply());
    }

    @Override
    public Maybe<LoginUser> addUser(Users users) {
        return mApi.addUser(users);
    }

    @Override
    public Completable setJWT(String accessToken, String refreshToken) {
        return Completable.fromAction(() -> {
            mPreference.edit().putString(LoginPreference.PREF_ACCESS_TOKEN.name(), accessToken).apply();
            mPreference.edit().putString(LoginPreference.PREF_REFRESH_TOKEN.name(), refreshToken).apply();
        });
    }
}