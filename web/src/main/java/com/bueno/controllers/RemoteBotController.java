package com.bueno.controllers;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.bot.usecase.RemoteBotRepositoryUseCase;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotAlreadyExistsException;
import com.bueno.domain.usecases.utils.exceptions.RemoteBotNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/remote-bots")
public class RemoteBotController {

    RemoteBotRepository repository;

    public RemoteBotController(RemoteBotRepository remoteBotRepository) {
        repository = remoteBotRepository;
    }

    @GetMapping("/{name}")
    private ResponseEntity<?> getRemoteBotByName(@PathVariable String name) {
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);

        try {
            RemoteBotDto bot = useCase.getByName(name);
            return ResponseEntity.ok(bot);
        } catch (RemoteBotNotFoundException e) {
            return ResponseEntity.badRequest().body("Bot: " + name + " not found");
        }
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<?> getRemoteBotsByUserName(@PathVariable String userId) {// TODO fazer metodo que pega todos os bots cadastrados por um usuário;
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);

        try {
            RemoteBotDto bot = useCase.getByName(userId);
            return ResponseEntity.ok(bot);
        } catch (RemoteBotNotFoundException e) {
            return ResponseEntity.badRequest().body("Bot: " + userId + " not found");
        }
    }

    @GetMapping
    private ResponseEntity<?> getAllRemoteOnes() {
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);
        return ResponseEntity.ok(useCase.getAll());
    }

    @PostMapping
    private ResponseEntity<?> addRemote(@RequestBody RemoteBotRequestType requestType) {// TODO tirar uuid por String
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);
        RemoteBotDto dto = new RemoteBotDto(UUID.randomUUID(),
                                                requestType.userId(),
                                                requestType.name(),
                                                requestType.url(),
                                                requestType.port());
        try {
            useCase.addBot(dto);
            return ResponseEntity.ok(dto);
        } catch (RemoteBotAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{name}")
    private ResponseEntity<?> update(@PathVariable String name, @RequestBody RemoteBotDto dto) { // TODO fazer um método update no RemoteBotRepositoryUseCase;
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);
        try {
            useCase.delete(name);
            return ResponseEntity.ok(dto);
        } catch (RemoteBotNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{name}")
    private ResponseEntity<?> delete(@PathVariable String name) {
        RemoteBotRepositoryUseCase useCase = new RemoteBotRepositoryUseCase(repository);
        try {
            useCase.delete(name);
            return ResponseEntity.ok("Bot: " + name + " successfully deleted");
        } catch (RemoteBotNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
