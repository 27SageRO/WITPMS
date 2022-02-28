package com.wittyly.witpms.interactor;

import com.wittyly.witpms.model.POJO.FeedModel;
import com.wittyly.witpms.repository.FeedRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetFeedUseCase extends UseCase<List<FeedModel>, Params> {

    private final FeedRepository feedRepository;

    @Inject
    public GetFeedUseCase(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    Observable<List<FeedModel>> buildUseCaseObservable(Params params) {
        return feedRepository.getFeed(
                params.getInt("page")
        );
    }
}
