package com.example.demowebmvc;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/events") // url 겹치지않게 클래스 위에 선언
public class EventApi {
    @PostMapping
    public Event createEvent(HttpEntity<Event> request){
        // event 저장
        MediaType conMediaType = request.getHeaders().getContentType();
        System.out.println(conMediaType);
        return request.getBody();
    }
}
