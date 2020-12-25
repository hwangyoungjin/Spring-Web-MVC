package com.springbootmvc.demo.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @GetMapping("/hello")
    public String hello(Model model){ //함수이름이 view의 이름이 된다.
        //view에다가 전달해야하는 객체는 Model에 담는다.
        model.addAttribute("name","young");
        return "hello"; //restController가 아니므로 응답 본문이 아니고 hello.html이 view name이 된다.

    }
}
