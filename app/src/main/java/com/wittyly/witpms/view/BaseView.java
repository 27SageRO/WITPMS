package com.wittyly.witpms.view;

public interface BaseView {
    void onDone();
    void onHalt(Throwable e);
}