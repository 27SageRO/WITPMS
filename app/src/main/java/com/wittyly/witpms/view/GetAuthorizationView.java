package com.wittyly.witpms.view;

import com.wittyly.witpms.model.Authorization;

public interface GetAuthorizationView extends BaseView {
    void onResult(Authorization authorization);
}