package com.wittyly.witpms.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    @PrimaryKey
    private int id;

    private String email;
    private String language;

    @SerializedName("first_name") private String firstname;
    @SerializedName("last_name") private String lastname;
    @SerializedName("user_name") private String username;
    @SerializedName("is_active") private int isActive;
    @SerializedName("email_verified") private int emailVerified;
    @SerializedName("image_path") private String imagePath;
    @SerializedName("is_blocked") private int isBlocked;
    @SerializedName("last_login_date") private Date lastLoginDate;
    @SerializedName("last_password_changed") private Date lastPasswordChanged;
    @SerializedName("date_created") private Date dateCreated;
    @SerializedName("date_modified") private Date dateModified;

    private Zone zone;
    private UserType type;
    private Level level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(int emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(int isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Date getLastPasswordChanged() {
        return lastPasswordChanged;
    }

    public void setLastPasswordChanged(Date lastPasswordChanged) {
        this.lastPasswordChanged = lastPasswordChanged;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getFullName() {
        return firstname + " " + lastname;
    }

}
