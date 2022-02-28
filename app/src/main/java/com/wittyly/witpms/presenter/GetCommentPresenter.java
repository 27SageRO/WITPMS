package com.wittyly.witpms.presenter;

import com.wittyly.witpms.interactor.GetCommentUseCase;
import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.Comment;
import com.wittyly.witpms.view.GetCommentView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class GetCommentPresenter implements Presenter {

    private final GetCommentUseCase useCase;
    private GetCommentView view;

    @Inject
    public GetCommentPresenter(GetCommentUseCase useCase) {
        this.useCase = useCase;
    }

    public void setView(GetCommentView view) {
        this.view = view;
    }

    public void start(Params params) {
        useCase.execute(new DisposableObserver<List<Comment>>() {
            @Override
            public void onNext(List<Comment> value) {
                if (view != null) {
                    for (Comment comment : value) {
                        view.onResult(comment);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (view != null) {
                    view.onHalt(e);
                }
            }

            @Override
            public void onComplete() {
                if (view != null) {
                    view.onDone();
                }
            }

        }, params);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        useCase.dispose();
        view = null;
    }

}
