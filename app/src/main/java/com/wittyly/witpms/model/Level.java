package com.wittyly.witpms.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Level extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;

}
