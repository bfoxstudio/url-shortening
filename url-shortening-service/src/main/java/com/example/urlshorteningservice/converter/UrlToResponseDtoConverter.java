package com.example.urlshorteningservice.converter;

import com.example.urlshorteningservice.dto.ResponseUrlDto;
import com.example.urlshorteningservice.model.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UrlToResponseDtoConverter implements Converter<Url, ResponseUrlDto> {

    @Value("${host.base-url}")
    private String baseUrl;

    @Override
    public ResponseUrlDto convert(Url source) {
        ResponseUrlDto result = new ResponseUrlDto();
        result.setShortUrl(baseUrl + source.getId());
        result.setUrl(source.getOriginalUrl());
        result.setUsageCounter(source.getUsageCounter());
        return result;
    }
}
