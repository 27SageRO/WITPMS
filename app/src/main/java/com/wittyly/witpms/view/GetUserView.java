package com.wittyly.witpms.view;


import com.wittyly.witpms.model.User;

public interface GetUserView extends BaseView {
    void onResult(User user);
}
