package com.example.demo.repositoryImpl;

import com.example.demo.config.NaverProperties;
import com.example.demo.model.Movie;
import com.example.demo.model.ResponseMovie;
import com.example.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieRepositoryImpl implements MovieRepository {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private NaverProperties naverProperties;
    @Override
    public List<Movie> findByQuery(String query) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Naver-Client-Id",naverProperties.getClientId());
        httpHeaders.add("X-Naver-Client-Secret",naverProperties.getClientSecret());

        String url = naverProperties.getMovieUrl()+"?query=" + query;

        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), ResponseMovie.class)
                .getBody()
                .getItems()
                .stream()
                .map(m->Movie.builder()
                    .title(m.getTitle())
                    .link(m.getLink())
                    .userRating(m.getUserRating())
                    .build())
                .collect(Collectors.toList());
    }
}
