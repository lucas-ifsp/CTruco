package com.bueno.domain.usecases.bot.providers;

import com.bueno.domain.usecases.bot.repository.RemoteBotDto;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import org.springframework.stereotype.Service;

@Service
public interface RemoteBotApi {

    boolean fetchMaoDeOnzeResponse(GameIntel intel, RemoteBotDto botData);
    boolean fetchRaiseRequestDecision(GameIntel intel, RemoteBotDto botData);
    CardToPlay fetchCardToPlay(GameIntel intel, RemoteBotDto botData) ;
    int fetchRaiseResponse(GameIntel intel, RemoteBotDto botData) ;
    boolean isHealthy();
}
