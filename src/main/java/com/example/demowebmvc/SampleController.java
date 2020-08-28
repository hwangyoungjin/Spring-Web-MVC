package com.example.demowebmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SampleController {


    @GetMapping("/events/form")
    public String eventForm(Model model){
        model.addAttribute("event",new Event());
        return "/events/form";
    }

    @PostMapping("/events")
    public String createEvent(@Validated @ModelAttribute Event event,
                              BindingResult bindingResult,
                              Model model){
        if(bindingResult.hasErrors()){
            return "/events/form";
        }

        //원래는 아래부분이 db의 저장하는 부분임
        /*List<Event> eventList = new ArrayList<>(); //list.html에서 eventList 참조하고 있음
        eventList.add(event);
        model.addAttribute("eventList",eventList); // model.addAttribute(eventList)와 동일(이름이 같다면 이렇게써도됨)
*/
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
