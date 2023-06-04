package com.example.urlshorteningservice.controller;

import com.example.urlshorteningservice.dto.UrlDto;
import com.example.urlshorteningservice.model.Url;
import com.example.urlshorteningservice.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final ConversionService conversionService;

    @GetMapping("/{shortUrl}")
    @Operation(summary = "Get URL for Redirect", description = "Get the original URL for the provided short URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "301", description = "Moved Permanently", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema()))
    })
    public RedirectView getUrlForRedirect(
            @Parameter(description = "Short URL", example = "d7dcca4")
            @PathVariable String shortUrl) {
        Optional<Url> originalUrl = urlService.getOriginalUrl(shortUrl);
        RedirectView redirectView;
        if (originalUrl.isPresent()) {
            urlService.increaseCounter(shortUrl);
            redirectView = new RedirectView(originalUrl.get().getOriginalUrl());
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        } else {
            redirectView = new RedirectView("/" + shortUrl);
            redirectView.setStatusCode(HttpStatus.NOT_FOUND);
        }
        return redirectView;
    }

    @PostMapping("/api/v1/url/create-short")
    @Operation(summary = "Get Short URL", description = "Generate a short URL for the provided long URL")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UrlDto.class)))
    public ResponseEntity<UrlDto> createShortUrl(
            @RequestBody() UrlDto urlDto) {
        Url url = urlService.getShortUrl(conversionService.convert(urlDto, Url.class));
        return ResponseEntity.ok(conversionService.convert(url, UrlDto.class));
    }

    @GetMapping("/api/v1/user/{userId}/urls")
    @Operation(summary = "Get All User URLs", description = "Get all URLs for a specific user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UrlDto.class))))
    public ResponseEntity<List<UrlDto>> getAllUserUrls(
            @Parameter(description = "UserID", example = "3bea2a50-5bb7-4119-9225-6a84cd7dcca4")
            @PathVariable String userId
    ) {
        List<Url> allUserUrls = urlService.getAllUserUrls(userId);
        List<UrlDto> urlsDto = allUserUrls.stream().map(x -> conversionService.convert(x, UrlDto.class)).toList();
        return ResponseEntity.ok(urlsDto);
    }

    @DeleteMapping("/api/v1/user/{userId}/urls/{shortUrl}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Disable URL", description = "Disable a URL for a specific user")
    public void disableUrl(
            @Parameter(description = "UserID", example = "3bea2a50-5bb7-4119-9225-6a84cd7dcca4")
            @PathVariable String userId,
            @Parameter(description = "ShorUrl", example = "d7dcca4")
            @PathVariable String shortUrl
    ) {
        urlService.disableUrl(shortUrl, userId);
    }
}
