package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMovie {
    private List<Item> items;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item{
        private String title;
        private String link;
        private String actor;
        private String director;
        private float userRating;
        //TODO: 필드추가
    }
}
