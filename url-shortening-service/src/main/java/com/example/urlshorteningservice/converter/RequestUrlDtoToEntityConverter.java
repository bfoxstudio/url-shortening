package com.example.urlshorteningservice.converter;


import com.example.urlshorteningservice.dto.RequestUrlDto;
import com.example.urlshorteningservice.model.Url;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RequestUrlDtoToEntityConverter implements Converter<RequestUrlDto, Url> {

    @Override
    public Url convert(RequestUrlDto source) {
        Url result = new Url();
        result.setUserId(source.getUserId());
        result.setOriginalUrl(source.getUrl());
        return result;
    }
}
