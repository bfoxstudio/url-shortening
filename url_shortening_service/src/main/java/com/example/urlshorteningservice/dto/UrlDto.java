package com.example.urlshorteningservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlDto {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("shortUrl")
    private String shortUrl;

    @JsonProperty("usageCounter")
    private long usageCounter;
}
