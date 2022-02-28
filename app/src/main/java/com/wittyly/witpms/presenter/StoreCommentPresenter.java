package com.wittyly.witpms.presenter;

import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.interactor.StoreCommentUseCase;
import com.wittyly.witpms.model.POJO.DefaultResponse;
import com.wittyly.witpms.view.BaseView;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class StoreCommentPresenter implements Presenter {

    private final StoreCommentUseCase useCase;
    private BaseView view;

    @Inject
    public StoreCommentPresenter(StoreCommentUseCase useCase) {
        this.useCase = useCase;
    }

    public void setView(BaseView view) {
        this.view = view;
    }

    public void start(Params params) {
        useCase.execute(new DisposableObserver<DefaultResponse>() {
            @Override
            public void onNext(DefaultResponse value) {}
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

    }

}
