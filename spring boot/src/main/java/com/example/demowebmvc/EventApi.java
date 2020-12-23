package com.example.demowebmvc;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/events") // url 겹치지않게 클래스 위에 선언
public class EventApi {
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody @Valid Event event,BindingResult bindingResult){
        // event 저장
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().build(); //에러 보내기
        }
        return ResponseEntity.ok().body(event);
    }
}
