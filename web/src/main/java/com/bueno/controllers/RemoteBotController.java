package com.bueno.controllers;

import com.bueno.domain.usecases.bot.dtos.RemoteBotResponseModel;
import com.bueno.domain.usecases.bot.dtos.RemoteBotRequestModel;
import com.bueno.domain.usecases.bot.usecase.AddBotRemoteBotRepositoryUseCase;
import com.bueno.domain.usecases.bot.usecase.DeleteRemoteBotRepositoryUseCase;
import com.bueno.domain.usecases.bot.usecase.GetRemoteBotRepositoryUseCase;
import com.bueno.domain.usecases.bot.usecase.UpdateRemoteBotRepositoryUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/remote-bots")
public class RemoteBotController {

    private final AddBotRemoteBotRepositoryUseCase addBotRemoteBotRepositoryUseCase;
    private final DeleteRemoteBotRepositoryUseCase deleteRemoteBotRepositoryUseCase;
    private final GetRemoteBotRepositoryUseCase getRemoteBotRepositoryUseCase;
    private final UpdateRemoteBotRepositoryUseCase updateRemoteBotRepositoryUseCase;

    @Autowired
    public RemoteBotController(AddBotRemoteBotRepositoryUseCase addBotRemoteBotRepositoryUseCase,
                               DeleteRemoteBotRepositoryUseCase deleteRemoteBotRepositoryUseCase,
                               GetRemoteBotRepositoryUseCase getRemoteBotRepositoryUseCase,
                               UpdateRemoteBotRepositoryUseCase updateRemoteBotRepositoryUseCase) {
        this.addBotRemoteBotRepositoryUseCase = addBotRemoteBotRepositoryUseCase;
        this.deleteRemoteBotRepositoryUseCase = deleteRemoteBotRepositoryUseCase;
        this.getRemoteBotRepositoryUseCase = getRemoteBotRepositoryUseCase;
        this.updateRemoteBotRepositoryUseCase = updateRemoteBotRepositoryUseCase;
    }

    @GetMapping
    private ResponseEntity<?> getAllRemoteOnes() {
        return ResponseEntity.ok(getRemoteBotRepositoryUseCase.getAll());
    }

    @GetMapping("/{name}")
    private ResponseEntity<?> getRemoteBotByName(@PathVariable String name) {
        return ResponseEntity.ok(getRemoteBotRepositoryUseCase.getByName(name));
    }

    @GetMapping("/user/{userId}")
    private ResponseEntity<?> getRemoteBotsByUserName(@PathVariable UUID userId) {
        return ResponseEntity.ok(getRemoteBotRepositoryUseCase.getByUserId(userId));
    }

    @PostMapping
    private ResponseEntity<?> addRemote(@RequestBody RemoteBotRequestModel dtoRequest) {
        System.out.println(dtoRequest);
        RemoteBotResponseModel responseDto =
                Objects.requireNonNull(addBotRemoteBotRepositoryUseCase.addBot(dtoRequest), "Not able to create a RemoteBotResponseModel");
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{name}")
    private ResponseEntity<?> update(@PathVariable String name, @RequestBody RemoteBotRequestModel dto) {
        RemoteBotResponseModel responseDto = updateRemoteBotRepositoryUseCase.update(name, dto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("{name}")
    private ResponseEntity<?> delete(@PathVariable String name) {
        deleteRemoteBotRepositoryUseCase.delete(name);
        return ResponseEntity.ok("Bot: " + name + " successfully deleted");
    }
}
