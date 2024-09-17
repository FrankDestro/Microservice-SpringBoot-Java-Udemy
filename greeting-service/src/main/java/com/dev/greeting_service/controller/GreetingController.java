package com.dev.greeting_service.controller;

import com.dev.greeting_service.configuration.GreetingConfiguration;
import com.dev.greeting_service.models.entities.Greeting;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@RestController
public class GreetingController {

    private static final String template = "%s, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final GreetingConfiguration greetingConfiguration;

    @RequestMapping("/greeting")
    public Greeting greeting(
            @RequestParam(value = "name",
                    defaultValue = "") String name) {

        if (name.isEmpty()) name = greetingConfiguration.getDefaultValue();

        return new Greeting(
                counter.incrementAndGet(),
                String.format(template, greetingConfiguration.getGreeting(), name)
        );
    }
}