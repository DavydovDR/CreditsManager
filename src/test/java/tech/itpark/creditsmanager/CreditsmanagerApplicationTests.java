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
        // getAll
        mockMvc.perform(get("/credits"))
                .andExpect(content().string(
                        "[{\"id\":1,\"name\":\"ipoteka\",\"sum\":240000000,\"createdDate\":\"2018-01-01\"}," +
                        "{\"id\":2,\"name\":\"avto\",\"sum\":100000000,\"createdDate\":\"2020-10-10\"}]"));

        // getById
        mockMvc.perform(get("/credits/1"))
                .andExpect(content().string(
                        "{\"id\":1,\"name\":\"ipoteka\",\"sum\":240000000,\"createdDate\":\"2018-01-01\"}"));

        // delete
        mockMvc.perform(delete("/credits/1"))
                .andExpect(content().string(
                        "{\"id\":1,\"name\":\"ipoteka\",\"sum\":240000000,\"createdDate\":\"2018-01-01\"}"));

        mockMvc.perform(get("/credits"))
                .andExpect(content().string(
                        "[{\"id\":2,\"name\":\"avto\",\"sum\":100000000,\"createdDate\":\"2020-10-10\"}]"));

        // create
        mockMvc.perform(
                post("/credits")
                        .contentType("application/json")
                        .content( "{\"id\":0,\"name\":\"remont\",\"sum\":50000000,\"createdDate\":\"2019-11-11\"}")
        ).andExpect(
                content().string("{\"id\":3,\"name\":\"remont\",\"sum\":50000000,\"createdDate\":\"2019-11-11\"}")
        );
    }

}