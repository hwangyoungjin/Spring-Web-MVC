package com.example.demowebmvc;


//Junit5 사용시에는 @RunWith(SpringRynnver.class) 어노테이션은 명시할 필요없다
//출처 : https://goddaehee.tistory.com/211

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

//    @Test
//    public void helloTest() throws  Exception{
//        mockMvc.perform(put("/hi"))
//                .andDo(print())
//                .andExpect(status().isOk()); //4번은 클라이언트가 허용하지않는 요청을 보냄
//    }

    @Test
    public void postEvent() throws Exception{ // 이름은 uri path로 받고
        mockMvc.perform(post("/events/name/youngjin")
                    .param("limit","-10")) //limit은 param으로
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("youngjin"))
                ;
    }
}