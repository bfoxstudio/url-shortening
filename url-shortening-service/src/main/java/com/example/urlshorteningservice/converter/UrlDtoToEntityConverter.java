package com.example.urlshorteningservice.converter;


import com.example.urlshorteningservice.dto.UrlDto;
import com.example.urlshorteningservice.model.Url;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UrlDtoToEntityConverter implements Converter<UrlDto, Url> {

    @Override
    public Url convert(UrlDto source) {
        Url result = new Url();
        result.setOriginalUrl(source.getUrl());
        return result;
    }
}
