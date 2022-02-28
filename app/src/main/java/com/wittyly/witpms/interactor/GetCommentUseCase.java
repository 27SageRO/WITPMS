package com.wittyly.witpms.interactor;

import com.wittyly.witpms.model.Comment;
import com.wittyly.witpms.repository.CommentRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetCommentUseCase extends UseCase<List<Comment>, Params> {

    private final CommentRepository commentRepository;

    @Inject
    public GetCommentUseCase(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    Observable<List<Comment>> buildUseCaseObservable(Params params) {
        return commentRepository.getComment(
                params.getInt("task_series_no")
        );
    }

}
