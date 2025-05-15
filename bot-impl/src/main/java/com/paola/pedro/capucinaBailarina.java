package com.paola.pedro;

import com.bueno.spi.service.BotServiceProvider;
import com.bueno.spi.model.GameIntel;

public abstract class capucinaBailarina implements BotServiceProvider {

    @Override
    public String getName() {
        return "com.paola.pedro.capucinaBailarina";
    }

    @Override
    public String getDescription() {
        return "Capucina Bailarina Bot";
    }

    @Override
    public String getAuthor() {
        return "Lucas B. R. de Oliveira";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void start() {
        // Implementação do bot
        System.out.println("Capucina Bailarina Bot started.");
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        // Implemente a lógica desejada aqui
        return true;
    }
}