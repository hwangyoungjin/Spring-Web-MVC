package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieGroup;
import com.example.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }


    public List<Movie> search(final String query){
        MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
        return movieGroup.getListOrderRating();
    }
}
