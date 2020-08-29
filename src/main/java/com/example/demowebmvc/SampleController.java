package com.example.demowebmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("event")
public class SampleController {

    @GetMapping("/events/form/name")
    public String eventFormName(Model model){
        model.addAttribute("event",new Event()); // @SessionAttribute를 통해 session에 들어간다.
        return "/events/form-name";
    }

    @PostMapping("/events/form/name")
    public String eventFormNameSubmit(@Validated @ModelAttribute Event event,
                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/events/form-name";
        }

        return "redirect:/events/form/limit"; //요청처리 /events/list" 으로 위임
    }

    @GetMapping("/events/form/limit")
    public String eventFormLimit(@ModelAttribute Event event, Model model){
        model.addAttribute("event", event);
        return "/events/form-limit";
    }

    @PostMapping("/events/form/limit")
    public String eventFormLimitSubmit(@Validated @ModelAttribute Event event,
                                     BindingResult bindingResult,
                                     SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "/events/form-limit";
        }

        //DB저장하는 곳

        //세션만료
        sessionStatus.setComplete();

        return "redirect:/events/list"; //요청처리 /events/list" 으로 위임
    }

    @GetMapping("/events/list")
    public String getEvents(Model model){

        //원래는 db에서 가져오는 부분
        Event event = new Event();
        event.setName("Spring");
        event.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(event);
        model.addAttribute("eventList",eventList);
        return "events/list";
    }
}
