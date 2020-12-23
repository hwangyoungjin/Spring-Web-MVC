package com.springbootmvc.demo.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    //해당 객체는 @WebMvcTest를 통해 자동으로 bean으로 만들어지는데 그 bean을 사용
    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("hello"));
    }

    //User를 생성하는 컨트롤러
    @Test
    public void createUser_JSON() throws Exception {
        String userJson = "{\"username\":\"young\",\"password\":\"123\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/users/create") // 해당 url로
                .contentType(MediaType.APPLICATION_JSON) //
                .accept(MediaType.APPLICATION_JSON) //받고싶은 데이터타입 명시 안해도 상관없으나 주면 더 좋음
                .content(userJson)) // json형식의 데이터를 request로 보낸다.
                    //응답결과가 json으로 나올것이므로 본문 username, password에 내가 넣어주었던 값이 young,123이 나오는지 확인
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("young"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("123"));
    }

}
