package com.wittyly.witpms.view;

import com.wittyly.witpms.model.Comment;

public interface GetCommentView extends BaseView {
    void onResult(Comment comment);
}
