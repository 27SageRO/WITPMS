package com.wittyly.witpms.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ticket extends RealmObject {


    @PrimaryKey
    private int id;

    private RealmList<TicketAssignees> assignees;
    private TicketCategory category;
    private Project project;

    private String title;
    private String description;

    @SerializedName("series_no") private int seriesNo;
    @SerializedName("generated_id") private String generatedId;
    @SerializedName("date_added") private Date dateAdded;
    @SerializedName("date_start") private Date dateStart;
    @SerializedName("date_due") private Date dateDue;
    @SerializedName("date_modified") private Date dateModified;
    @SerializedName("date_last_activity") private Date dateLastActivity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(int seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    public RealmList<TicketAssignees> getAssignees() {
        return assignees;
    }

    public void setAssignees(RealmList<TicketAssignees> assignees) {
        this.assignees = assignees;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public void setDateDue(Date dateDue) {
        this.dateDue = dateDue;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public Date getDateLastActivity() {
        return dateLastActivity;
    }

    public void setDateLastActivity(Date dateLastActivity) {
        this.dateLastActivity = dateLastActivity;
    }
}