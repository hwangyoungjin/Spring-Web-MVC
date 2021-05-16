package com.example.demo.swagger.api;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestApiController {

    @GetMapping("/test")
    public String test(){
        return "hello";
    }

    @GetMapping("/test1")
    public String giveMe(@RequestParam String hello){
        return "who";
    }

    @PostMapping("/test")
    public String test(@RequestParam String hello){
        return "post";
    }
}
