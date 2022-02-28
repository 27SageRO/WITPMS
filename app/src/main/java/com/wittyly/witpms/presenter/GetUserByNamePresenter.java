package com.wittyly.witpms.presenter;

import com.wittyly.witpms.interactor.GetUserByNameUseCase;
import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.User;
import com.wittyly.witpms.view.GetUserView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class GetUserByNamePresenter implements Presenter {

    private final GetUserByNameUseCase useCase;
    private GetUserView view;

    @Inject
    public GetUserByNamePresenter(GetUserByNameUseCase useCase) {
        this.useCase = useCase;
    }

    public void setView(GetUserView view) {
        this.view = view;
    }

    public void start(Params params) {
        useCase.execute(new DisposableObserver<List<User>>() {
            @Override
            public void onNext(List<User> users) {
                if (view != null) {
                    for (User user: users) {
                        view.onResult(user);
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
