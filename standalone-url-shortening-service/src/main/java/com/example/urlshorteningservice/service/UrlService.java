package com.example.urlshorteningservice.service;

import com.example.urlshorteningservice.model.Url;
import com.example.urlshorteningservice.repository.UrlRepository;
import com.example.urlshorteningservice.utils.Sha1Generator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    private final Random rand = new Random();
    private final static int SHA1_LENGTH = 40;

    @Value("${url-params.length}")
    private Integer shortUrlLength;

    @Value("${url-params.attempts}")
    private Integer attemptsCount;

    @Transactional
    public Url getShortUrl(Url url) {
        for (int i = 0; i < attemptsCount; i++) {
            String saltedUrl = url.getOriginalUrl() + rand.nextInt();
            String hash = Sha1Generator.getHash(saltedUrl);
            int substringStart = 0;
            int substringEnd = shortUrlLength;
            while (substringEnd < SHA1_LENGTH) {
                String shortUrl = hash.substring(substringStart, substringEnd);
                url.setId(shortUrl);
                boolean exists = urlRepository.existsById(shortUrl);
                if (!exists) {
                    urlRepository.save(url);
                    return url;
                }
                url.setInsertAttempts(url.getInsertAttempts() + 1);
                substringStart += shortUrlLength;
                substringEnd += shortUrlLength;
            }
        }
        throw new RuntimeException("failed to generate short url");
    }

    @Transactional(readOnly = true)
    public Optional<Url> getOriginalUrl(String id) {
        Optional<Url> dbResponse = urlRepository.findByIdAndIsActiveTrue(id);
        if (dbResponse.isEmpty()) {
            return Optional.empty();
        }
        Url url = dbResponse.get();
        return Optional.of(url);
    }

    @Transactional
    public void increaseCounter(String shortUrl) {
        urlRepository.increaseCounter(shortUrl);
    }

    @Transactional(readOnly = true)
    public List<Url> getAllUserUrls(String userId) {
        return urlRepository.findAllByUserIdAndIsActiveTrue(userId);
    }

    @Transactional
    public void disableUrl(String shortUrl, String userId) {
        urlRepository.disableUrl(shortUrl, userId);
    }
}
