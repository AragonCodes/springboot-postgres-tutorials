package com.example.springbootpostgrestutorials.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tutorial_details")
public class TutorialDetails {

    @Id
    private Long id;

    @Column
    private Date createdOn;

    @Column
    private String createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tutorial_id")
    private Tutorial tutorial;

    public TutorialDetails(){}

    public TutorialDetails(String createdBy) {
        this.createdOn = new Date();
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Tutorial getTutorial() {
        return tutorial;
    }

    public void setTutorial(Tutorial tutorial) {
        this.tutorial = tutorial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Tutorial [tutorial=" + tutorial.toString() + ", createdOn=" + createdOn + ", createdBy=" + createdBy + "]";
    }
}
