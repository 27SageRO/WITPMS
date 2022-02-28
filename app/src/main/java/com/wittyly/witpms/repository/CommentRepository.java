package com.wittyly.witpms.repository;

import com.wittyly.witpms.model.Comment;
import com.wittyly.witpms.model.POJO.DefaultResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommentRepository {

    @GET("v2/comment")
    Observable<List<Comment>> getComment(
        @Query("task_series_no") int taskSeriesNo
    );

    @FormUrlEncoded
    @POST("v2/comment")
    Observable<DefaultResponse> storeComment(
        @Field("user_id") int userId,
        @Field("task_series_no") int taskSeriesNo,
        @Field("description") String description
    );

}
