package tech.itpark.creditsmanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class CreditsmanagerApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void complexTest() throws Exception {

        mockMvc.perform(get("/credits"))
                .andExpect(content().json("""
                        [{
                             "id": 1,
                             "name": "ipoteka",
                             "sum": 240000000,
                             "createdDate": "2018-01-01",
                             "payDay": 28,
                             "percent": 92,
                             "months": 180
                           },
                           {
                             "id": 2,
                             "name": "avto",
                             "sum": 100000000,
                             "createdDate": "2020-10-10",
                             "payDay": 20,
                             "percent": 142,
                             "months": 120
                           }]"""));


        mockMvc.perform(get("/credits/1"))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "name": "ipoteka",
                          "sum": 240000000,
                          "createdDate": "2018-01-01",
                          "payDay": 28,
                          "percent": 92,
                          "months": 180
                        }"""));


        mockMvc.perform(delete("/credits/1"))
                .andExpect(content().json("""
                        {
                          "id": 1,
                          "name": "ipoteka",
                          "sum": 240000000,
                          "createdDate": "2018-01-01",
                          "payDay": 28,
                          "percent": 92,
                          "months": 180
                        }"""));

        mockMvc.perform(get("/credits"))
                .andExpect(content().json("""
                        [
                           {
                             "id": 2,
                             "name": "avto",
                             "sum": 100000000,
                             "createdDate": "2020-10-10",
                             "payDay": 20,
                             "percent": 142,
                             "months": 120
                           }
                        ]"""));


        mockMvc.perform(post("/credits")
                .contentType("application/json")
                .content("""
                        {
                          "id": 0,
                          "name": "Кредит на ремонт",
                          "sum": 38637100,
                          "createdDate": "2020-11-15",
                          "payDay": 14,
                          "percent": 124,
                          "months": 48
                        }"""))
                .andExpect(content().json("""
                        {
                          "id": 3,
                          "name": "Кредит на ремонт",
                          "sum": 38637100,
                          "createdDate": "2020-11-15",
                          "payDay": 14,
                          "percent": 124,
                          "months": 48
                        }"""));

        mockMvc.perform(get("/credits/payments"))
                .andExpect(content().json("""
                          [
                          {
                            "id": 1,
                            "paySum": 500000,
                            "payDate": "2020-01-01",
                            "mainDebt": 250000,
                            "creditId": 2
                          },
                          {
                            "id": 2,
                            "paySum": 500000,
                            "payDate": "2020-02-01",
                            "mainDebt": 250000,
                            "creditId": 2
                          }
                        ]"""));
    }
}