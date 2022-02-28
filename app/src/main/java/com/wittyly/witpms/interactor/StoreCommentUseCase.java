package com.wittyly.witpms.interactor;

import com.wittyly.witpms.model.Comment;
import com.wittyly.witpms.model.POJO.DefaultResponse;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.repository.CommentRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

public class StoreCommentUseCase extends UseCase<DefaultResponse, Params> {

    private final CommentRepository commentRepository;
    private final User user;

    @Inject
    public StoreCommentUseCase(CommentRepository commentRepository, User user) {
        this.commentRepository = commentRepository;
        this.user = user;
    }

    @Override
    Observable<DefaultResponse> buildUseCaseObservable(Params params) {
        return commentRepository.storeComment(
            user.getId(),
            params.getInt("task_series_no"),
            params.getString("description")
        );
    }

}
