package com.lucasjwilber.codefellowship;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ApplicationUserPost {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;

    @ManyToOne
    @JoinColumn(name="user.id")
    public ApplicationUser user;

    @Column(columnDefinition = "text")
    String body;
    String createdAt;

    ApplicationUserPost() {};

    ApplicationUserPost(String body, ApplicationUser user) {
        this.body = body;
        this.user = user;
        this.createdAt = new Date().toString();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ApplicationUser getUser() {
        return user;
    }
    public void setUser(ApplicationUser user) {
        this.user = user;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
