package de.bergmann.runnertracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class TestController {

    @GetMapping("/test")
    public String testHelloWorld() {
        return """
                Hello world!
                I think you are authenticated!
                """;
    }
}