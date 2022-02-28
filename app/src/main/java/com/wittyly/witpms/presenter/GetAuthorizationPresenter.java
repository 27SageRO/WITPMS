package com.wittyly.witpms.presenter;

import com.wittyly.witpms.interactor.GetAuthorizationUseCase;
import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.Authorization;
import com.wittyly.witpms.view.GetAuthorizationView;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class GetAuthorizationPresenter implements Presenter {

    private final GetAuthorizationUseCase useCase;
    private GetAuthorizationView view;

    @Inject
    public GetAuthorizationPresenter(GetAuthorizationUseCase useCase) {
        this.useCase = useCase;
    }

    public void setView(GetAuthorizationView getAuthorizationView) {
        this.view = getAuthorizationView;
    }

    public void start(Params params) {
        useCase.execute(new DisposableObserver<Authorization>() {
            @Override
            public void onNext(Authorization authorization) {
                if (view != null) {
                    view.onResult(authorization);
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
        view = null;
        useCase.dispose();
    }

}
