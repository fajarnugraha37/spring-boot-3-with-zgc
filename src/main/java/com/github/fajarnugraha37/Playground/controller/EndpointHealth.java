package com.github.fajarnugraha37.Playground.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointHealth {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
