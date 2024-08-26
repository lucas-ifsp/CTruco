package com.bueno.controllers;

import com.bueno.domain.usecases.bot.providers.BotManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bots")
public class BotController {
    BotManagerService provider;

    public BotController(BotManagerService provider) {
        this.provider = provider;
    }

    @GetMapping
    private List<String> getBotNames(){
        return provider.providersNames();
    }

    //TODO - fazer endpoint Evaluate Bots

    //TODO - fazer endpoint Rank Bots -> hall da fama

}
