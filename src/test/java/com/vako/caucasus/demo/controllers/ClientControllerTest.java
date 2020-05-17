package com.vako.caucasus.demo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void payByPhoneNumber_wrong_amount() throws Exception {

        mockMvc.perform(get("/mobile/pay")
                .param("number", "995970088")
                .param("amount", "200"))
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest())
                .andDo(print());
    }

    @Test
    void payByPhoneNumber_wrong_number() throws Exception {
        mockMvc.perform(get("/mobile/pay")
                .param("number", "9959700888888")
                .param("amount", "100"))
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest())
                .andDo(print());
    }

    @Test
    void payById_wrong_id() throws Exception {
        mockMvc.perform(get("/id/pay")
                .param("number", "0123456789555")
                .param("firstName", "vako")
                .param("lastName", "vardishvili")
                .param("amount", "80"))
                .andExpect(MockMvcResultMatchers.status()
                        .isBadRequest())
                .andDo(print());
    }
}
