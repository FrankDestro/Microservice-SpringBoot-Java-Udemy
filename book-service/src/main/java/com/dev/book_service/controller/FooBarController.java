package com.dev.book_service.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Foo bar")
@RequiredArgsConstructor
@RestController
@RequestMapping("book-service")
public class FooBarController {

    private Logger looger = LoggerFactory.getLogger(FooBarController.class);

    @Operation(summary = "Foo Bar", method = "GET")
    @GetMapping("/foo-bar")
//    @Retry(name = "foo-bar", fallbackMethod = "fallbackMethod")
//    @CircuitBreaker(name = "default", fallbackMethod = "fallbackMethod")
//    @RateLimiter(name = "foo-bar-rate")
    @Bulkhead(name = "foo-bar-bk")
    public String fooBar() {
       looger.info("Request to foo-bar is received!");
//       var response =  new RestTemplate().getForEntity("http://localhost:8080/foo-bar", String.class);
        return "Foo-Bar!!!";
//       return response.getBody();
    }

    public String fallbackMethod(Exception e) {
        return "fallbackMethod foo-bar!!!";
    }
}
