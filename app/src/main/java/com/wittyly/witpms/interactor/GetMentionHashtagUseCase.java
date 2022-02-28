package com.wittyly.witpms.interactor;

import com.wittyly.witpms.model.POJO.HashtagMentionModel;
import com.wittyly.witpms.repository.MentionRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetMentionHashtagUseCase extends UseCase<HashtagMentionModel, Params> {

    private final MentionRepository mentionRepository;

    @Inject
    public GetMentionHashtagUseCase(MentionRepository mentionRepository) {
        this.mentionRepository = mentionRepository;
    }

    @Override
    Observable<HashtagMentionModel> buildUseCaseObservable(Params params) {
        return mentionRepository.getMentionHashtag(
                params.getString("value"),
                params.getInt("page")
        );
    }

}
