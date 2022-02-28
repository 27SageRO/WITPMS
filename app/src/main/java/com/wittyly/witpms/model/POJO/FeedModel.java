package com.wittyly.witpms.model.POJO;

import com.wittyly.witpms.model.Task;
import com.wittyly.witpms.model.Ticket;

public class FeedModel {

    private Task task;
    private Ticket ticket;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
