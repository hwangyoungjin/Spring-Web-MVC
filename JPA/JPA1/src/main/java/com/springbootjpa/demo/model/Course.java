package com.springbootjpa.demo.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {

    @Id @GeneratedValue
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "likedCourses")
    Set<Student> likes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Student> getLikes() {
        return likes;
    }

    public void setLikes(Set<Student> likes) {
        this.likes = likes;
    }
}
