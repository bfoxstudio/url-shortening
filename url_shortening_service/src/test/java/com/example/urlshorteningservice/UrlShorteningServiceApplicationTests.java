package com.example.urlshorteningservice;

import com.example.urlshorteningservice.dto.UrlDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShorteningServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test()
    @DisplayName("Test shortUrl creation and redirect")
    void shouldCreateAndReceiveURL() throws Exception {
        UrlDto urlDto = UrlDto.builder()
                .url("https://media.tenor.com/x8v1oNUOmg4AAAAd/rickroll-roll.gif")
                .userId("3bea2a50-5bb7-4119-9225-6a84cd7dcca4")
                .build();
        String getUrlRequestBody = objectMapper.writeValueAsString(urlDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/url/create-short")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getUrlRequestBody)
                ).andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        UrlDto urlCreationResponse = objectMapper.readValue(content, UrlDto.class);

        Assertions.assertEquals(urlDto.getUrl(), urlCreationResponse.getUrl());
        Assertions.assertEquals(urlDto.getUserId(), urlCreationResponse.getUserId());
        Assertions.assertTrue(Strings.isNotEmpty(urlCreationResponse.getUserId()));

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(urlCreationResponse.getShortUrl())
                ).andExpect(status().isMovedPermanently())
                .andReturn();

        String redirectLocation = mvcResult.getResponse().getHeader("Location");
        Assertions.assertEquals(urlDto.getUrl(), redirectLocation);
        System.out.println();
    }
}
