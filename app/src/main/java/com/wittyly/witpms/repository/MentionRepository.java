package com.wittyly.witpms.repository;

import com.wittyly.witpms.model.POJO.HashtagMentionModel;
import com.wittyly.witpms.model.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MentionRepository {

    @GET("v2/mention/hashtag")
    Observable<HashtagMentionModel> getMentionHashtag(
            @Query("value") String value,
            @Query("page") int page
    );

}
