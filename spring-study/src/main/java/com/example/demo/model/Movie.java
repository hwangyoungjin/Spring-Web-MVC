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
}
