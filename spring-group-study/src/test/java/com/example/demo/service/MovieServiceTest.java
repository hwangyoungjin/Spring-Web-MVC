package com.example.demo.service;

import com.example.demo.config.NaverProperties;
import com.example.demo.model.Movie;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repositoryImpl.MovieRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

class MovieServiceTest {
    //첫 번째 테스트 방법 Mock클래스 직접 생성
    class MockMovieRepositoryImpl extends MovieRepositoryImpl{

         public MockMovieRepositoryImpl(RestTemplate restTemplate, NaverProperties naverProperties){
             super(restTemplate,naverProperties);
         }

        @Override //가짜데이터를 반환
        public List<Movie> findByQuery(String query) {
            return Arrays.asList(
                    Movie.builder().title("영화1").link("http://test").userRating(9.3f).build(),
                    Movie.builder().title("영화2").link("http://test").userRating(8.7f).build(),
                    Movie.builder().title("영화3").link("http://test").userRating(9.7f).build()
            );
        }
    }

    @DisplayName("평점 순으로 정렬되는지 검사")
    @Test
    void shouldSortedInOrderOfGrade(){
        //given
        String query = "테스트_쿼리";
        String expectedTopRankingMovieTitle = "영화3";
        MovieRepository movieRepository = new MockMovieRepositoryImpl(null,null);
        MovieService movieService = new MovieService(movieRepository);

        //when
        List<Movie> movieList = movieService.search(query);

        //then
        Assertions.assertEquals(expectedTopRankingMovieTitle, movieList.stream().findFirst().get().getTitle());
    }
}