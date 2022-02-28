package com.wittyly.witpms.repository;

import com.wittyly.witpms.model.Authorization;
import com.wittyly.witpms.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserRepository {

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<Authorization> getAuthorization(
        @Field("grant_type") String grantType,
        @Field("client_id") int clientId,
        @Field("client_secret") String clientSecret,
        @Field("username") String username,
        @Field("password") String password,
        @Field("scope") String scope
    );

    @GET("v2/user/access")
    Observable<User> getUserByAccess();

    @GET("v2/user/mention")
    Observable<List<User>> getMentions(
        @Query("value") String name
    );

    @GET("nonblock/user/username")
    Observable<User> getUserByUsername(
        @Query("value") String username
    );

}