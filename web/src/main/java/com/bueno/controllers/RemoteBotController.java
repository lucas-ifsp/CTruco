package com.bueno.controllers;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.bot.usecase.RemoteBotRepositoryUseCase;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotAlreadyExistsException;
import com.bueno.domain.usecases.utils.exceptions.DtoNotStringableException;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("remote-bots")
public class RemoteBotController { //TODO receber url,porta,email. Usar postman

    RemoteBotRepository repository;

    @Autowired
    public RemoteBotController(RemoteBotRepository remoteBotRepository) {
        repository = remoteBotRepository;
    }

    @GetMapping("{name}")
    private ResponseEntity<?> getByUUID(@PathVariable String name){
        final var responseModel = repository.findAll().stream()
                .filter(remoteBotDto -> remoteBotDto.name().equals(name)).limit(1)
                .findAny()
                .orElse(null);
        if (responseModel == null) return ResponseEntity.badRequest().body("Bot: " + name + " not found");
        return ResponseEntity.ok(responseModel);
    }

    @GetMapping("all-bots")
    private ResponseEntity<?> getAll() {
        final var responseModel = repository.findAll();
        return ResponseEntity.ok(responseModel);
    }

    @PostMapping()
    private ResponseEntity<?> addRemote(@RequestBody RemoteBotDto dto) {
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);
        try {
            useCase.addBot(dto);
            return ResponseEntity.ok(dto);
        } catch (DtoNotStringableException | RemoteBotAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("{name}")
    private ResponseEntity<?> update(@PathVariable String name,@RequestBody RemoteBotDto dto) {
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);
        try {
            useCase.delete(name);
            return ResponseEntity.ok(dto);
        } catch (DtoNotStringableException | RemoteBotNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{name}")
    private ResponseEntity<?> delete(@PathVariable String name) {
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);
        try {
            useCase.delete(name);
            return ResponseEntity.ok("Bot: " + name + " successfully deleted");
        } catch (DtoNotStringableException | RemoteBotNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
