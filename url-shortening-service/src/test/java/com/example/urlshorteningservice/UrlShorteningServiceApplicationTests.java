package com.example.urlshorteningservice;

import com.example.urlshorteningservice.dto.RequestUrlDto;
import com.example.urlshorteningservice.dto.ResponseUrlDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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

    @MockBean
    private Jwt mockJwt;

    @BeforeEach
    void setup() {
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(mockJwt, null));
    }

    @Test()
    @DisplayName("Test shortUrl creation and redirect")
    void shouldCreateAndReceiveURL() throws Exception {
        RequestUrlDto urlDto = RequestUrlDto.builder()
                .url("https://example.com")
                .build();
        String getUrlRequestBody = objectMapper.writeValueAsString(urlDto);

        Jwt mockJwt = Mockito.mock(Jwt.class);
        Mockito.when(mockJwt.getSubject()).thenReturn("user123");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/url/create-short")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getUrlRequestBody)
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(mockJwt))
                ).andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        ResponseUrlDto urlCreationResponse = objectMapper.readValue(content, ResponseUrlDto.class);

        Assertions.assertEquals(urlDto.getUrl(), urlCreationResponse.getUrl());
        Assertions.assertTrue(Strings.isNotEmpty(urlCreationResponse.getShortUrl()));

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(urlCreationResponse.getShortUrl())
                ).andExpect(status().isMovedPermanently())
                .andReturn();

        String redirectLocation = mvcResult.getResponse().getHeader("Location");
        Assertions.assertEquals(urlDto.getUrl(), redirectLocation);
        System.out.println();
    }
}
