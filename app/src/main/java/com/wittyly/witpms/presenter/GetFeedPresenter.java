package com.wittyly.witpms.presenter;

import com.wittyly.witpms.interactor.GetFeedUseCase;
import com.wittyly.witpms.interactor.Params;
import com.wittyly.witpms.model.POJO.FeedModel;
import com.wittyly.witpms.view.GetFeedView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;
import io.realm.Realm;

public class GetFeedPresenter implements Presenter {

    private final GetFeedUseCase useCase;
    private final Realm realm;
    private GetFeedView view;

    @Inject
    public GetFeedPresenter(GetFeedUseCase useCase, Realm realm) {
        this.useCase = useCase;
        this.realm = realm;
    }

    public void setView(GetFeedView view) {
        this.view = view;
    }

    public void start(Params params) {
        useCase.execute(new DisposableObserver<List<FeedModel>>() {
            @Override
            public void onNext(List<FeedModel> feeds) {
                if (view != null) {
                    for (final FeedModel feed : feeds) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(feed.getTask());
                            }
                        });
                        view.onResult(feed);
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
