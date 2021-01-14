package com.example.demo.model;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Builder
@Getter
public class Movie implements Serializable {
    private String title;
    private String link;
    private float userRating;
    private String image;
    private String pubDate;
    private String actor;
    private String director;
    //titile, link, userRating, image, pubDate, actor, director
}
