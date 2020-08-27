package com.example.demowebmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class SampleController {

    @RequestMapping("/hello/{name=[a-z]+}")
    @ResponseBody
    public String hello(@PathVariable String name){
        return "hello"+name;
    }

    @GetMapping("/event/form")
    public String eventForm(Model model){
        model.addAttribute("event",new Event());
        return "/events/form";
    }

    @PostMapping("/events/name/{name}")
    @ResponseBody
    public Event getEvent(@Validated(Event.ValidateName.class)@ModelAttribute Event event, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            event.setComent("에러발생:"+bindingResult.getAllErrors());
            System.out.println("==========================");
            bindingResult.getAllErrors().forEach(c-> System.out.println(c.toString()));
        }
        return event;
    }

}
