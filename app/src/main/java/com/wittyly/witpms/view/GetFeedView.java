package com.wittyly.witpms.view;

import com.wittyly.witpms.model.POJO.FeedModel;

public interface GetFeedView extends BaseView {
    void onResult(FeedModel feed);
}
