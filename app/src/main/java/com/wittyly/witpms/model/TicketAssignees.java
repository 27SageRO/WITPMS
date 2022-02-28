package com.wittyly.witpms.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TicketAssignees extends RealmObject {

    @PrimaryKey
    private int id;

    private Ticket ticket;
    private User user;
    
}