package com.example.urlshorteningservice.converter;

import com.example.urlshorteningservice.dto.UrlDto;
import com.example.urlshorteningservice.model.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UrlToDtoConverter implements Converter<Url, UrlDto> {

    @Value("${host.base-url}")
    private String baseUrl;

    @Override
    public UrlDto convert(Url source) {
        UrlDto result = new UrlDto();
        result.setShortUrl(baseUrl + source.getId());
        result.setUrl(source.getOriginalUrl());
        result.setUsageCounter(source.getUsageCounter());
        return result;
    }
}
