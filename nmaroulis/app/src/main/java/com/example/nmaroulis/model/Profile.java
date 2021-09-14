package com.example.nmaroulis.model;

import javax.persistence.*;

@Entity
@Table
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long profileid;

    @Column(name = "name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "surname", nullable = false, length = 45)
    private String surName;

    public Profile(String firstName, String surName) {
        this.firstName = firstName;
        this.surName = surName;
    }

    public Profile() {
    }

    public Long getId() {
        return profileid;
    }

    public void setId(Long id) {
        this.profileid = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return surName;
    }

    public void setLastName(String lastName) {
        this.surName = surName;
    }

    @Override
    public String toString() {
        return "{" +
                "profileid=" + profileid +
                ", name='" + firstName + '\'' +
                ", surname='" + surName + '\'' +
                '}';
    }
}