package com.springbootmvc.demo.user;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@WebMvcTest(SampleController.class) //test하려는 class이름
public class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        //요청 : "/"
        //응답
        //-model name : young
        //-view name : hello
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                //Controller에서 return하는 view name이 hello
                .andExpect(MockMvcResultMatchers.view().name("hello"))
                //Controller에서 return에서 name이라는 이름의 model 값이 young인지
                .andExpect(MockMvcResultMatchers.model().attribute("name","young"))
                //view에서 young이라는 content를 포함하는지 확인
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("young")));
    }

}