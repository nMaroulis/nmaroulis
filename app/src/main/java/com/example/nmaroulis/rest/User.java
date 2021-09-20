package com.example.nmaroulis.rest;

import java.util.List;

public class User {

    private int id;

    private String fname;
    private String lname;
    private String username;
    private String email;
    private String phone;
    private String residence;
    private String education;
    private String work;
    private String gender;
    private List<Post> posts;
    private Title title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String ecucation) {
        this.education = education;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResidense() {
        return residence;
    }

    public void setResidense(String residence) {
        this.residence = residence;
    }


    public String getFullName() {
        return fname + " " + lname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);

    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", username='" + username + '\'' +
                ", title=" + title +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", residence=" + residence +
                ", education=" + education +
                ", work='" + work + '\'' +
                ", gender='" + gender + '\'' +
                ", title=" + title +
                '}';
    }
}