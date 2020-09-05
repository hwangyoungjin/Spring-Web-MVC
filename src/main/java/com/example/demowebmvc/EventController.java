package com.example.demowebmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("event")
public class EventController {

    @ExceptionHandler
    public String eventErrorHandler(EventException eventException, Model model){
        model.addAttribute("message", "error 발생시키기");
        return "/error";
    }

    @GetMapping("/events/create")
    public String eventCreate(){
        throw new EventException();
        //return "/event/form-data";
    }

    @GetMapping("/events/form/name")
    public String eventFormName(Model model){
        // @SessionAttribute를 통해 session에 들어간다.
        model.addAttribute("event",new Event());
        return "/events/form-name";
    }

    @PostMapping("/events/form/name")
    public String eventFormNameSubmit(@Validated @ModelAttribute Event event,
                                     BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/events/form-name";
        }

        return "redirect:/events/form/limit"; //요청처리 GET("/events/for/limit") 으로 위임
    }

    @GetMapping("/events/form/limit")
    public String eventFormLimit(@ModelAttribute Event event, Model model){
        model.addAttribute("event", event);
        return "/events/form-limit";
    }

    @PostMapping("/events/form/limit")
    public String eventFormLimitSubmit(@Validated @ModelAttribute Event event,
                                       BindingResult bindingResult,
                                       SessionStatus sessionStatus,
                                       RedirectAttributes attributes){
        if(bindingResult.hasErrors()){
            return "/events/form-limit";
        }

        //DB저장하는 곳

        //세션만료
        sessionStatus.setComplete();

        //Flash Attuibute는 Session에 1회성(redirection으로 전달되는곳에서 처리하면 사라짐)으로 들어간다
        //url 경로에 영향x
        attributes.addFlashAttribute("newEvent",event);

        return "redirect:/events/list"; //요청처리 GET("/events/list") 으로 위임
    }

    @GetMapping("/events/list")
    public String getEvents(Model model,
                            @SessionAttribute LocalDateTime visitTime){
        //visitTime 이름 동일해야함
        System.out.println(visitTime);

        //원래는 db에서 가져오는 부분

        //Flash Attuibute으로 추가한 객체가 model에 담긴다.
        Event redirectEvent = (Event) model.asMap().get("newEvent");

        Event stringEvent = new Event();
        stringEvent.setName("Spring");
        stringEvent.setLimit(10);

        List<Event> eventList = new ArrayList<>();
        eventList.add(stringEvent);
        eventList.add(redirectEvent);
        model.addAttribute("eventList",eventList);
        return "/events/list";
    }
}
