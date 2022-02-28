package com.wittyly.witpms.model.POJO;

import com.google.gson.annotations.SerializedName;
import com.wittyly.witpms.model.Project;
import com.wittyly.witpms.model.Ticket;
import com.wittyly.witpms.model.TicketCategory;

import java.util.List;

public class HashtagMentionModel {

    private List<Project> projects;
    private List<Ticket> tickets;

    @SerializedName("ticket_categories") private List<TicketCategory> ticketCategories;

    public List<TicketCategory> getTicketCategories() {
        return ticketCategories;
    }

    public void setTicketCategories(List<TicketCategory> ticketCategories) {
        this.ticketCategories = ticketCategories;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

}
