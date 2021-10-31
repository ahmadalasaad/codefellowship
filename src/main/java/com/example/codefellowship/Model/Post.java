package com.example.codefellowship.Model;


import javax.persistence.*;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String post;

    @ManyToOne
    public AppUser appUser;

    public Post(String post, AppUser appUser) {
        this.post = post;
        this.appUser = appUser;
    }
    public Post(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public AppUser getAppUser() {
        return appUser;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}
