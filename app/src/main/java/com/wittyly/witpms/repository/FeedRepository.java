package com.wittyly.witpms.repository;

import com.wittyly.witpms.model.POJO.FeedModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FeedRepository {

    @GET("v2/feed")
    Observable<List<FeedModel>> getFeed(
            @Query("page") int page
    );

}
