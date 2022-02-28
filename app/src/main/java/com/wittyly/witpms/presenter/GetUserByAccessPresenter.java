package com.wittyly.witpms.presenter;

import com.wittyly.witpms.interactor.GetUserByAccessUseCase;
import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.view.GetUserView;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class GetUserByAccessPresenter implements Presenter {

    private final GetUserByAccessUseCase useCase;
    private GetUserView view;

    @Inject
    public GetUserByAccessPresenter(GetUserByAccessUseCase useCase) {
        this.useCase = useCase;
    }

    public void setView(GetUserView view) {
        this.view = view;
    }

    public void start(Params params) {
        useCase.execute(new DisposableObserver<User>() {

            @Override
            public void onNext(User value) {
                if (view != null) {
                    view.onResult(value);
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
