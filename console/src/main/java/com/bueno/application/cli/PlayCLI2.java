/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.application.cli;

import com.bueno.application.cli.commands.CardModeReader;
import com.bueno.application.cli.commands.CardReader;
import com.bueno.application.cli.commands.IntelPrinter;
import com.bueno.application.cli.commands.RaiseRequestReader;
import com.bueno.application.standalone.InMemoryGameRepository;
import com.bueno.domain.entities.deck.Card;
import com.bueno.domain.entities.hand.HandScore;
import com.bueno.domain.entities.hand.Intel;
import com.bueno.domain.entities.player.mineirobot.MineiroBot;
import com.bueno.domain.entities.player.util.Player;
import com.bueno.domain.usecases.game.CreateGameUseCase;
import com.bueno.domain.usecases.hand.PlayHandUseCase;

import java.util.UUID;

import static com.bueno.application.cli.commands.CardModeReader.CardMode.OPEN;
import static com.bueno.application.cli.commands.RaiseRequestReader.RaiseChoice.REQUEST;


//TODO port remaining PlayerCLI features to PlayCLI2 class
//TODO check if it is useful to apply PossibleActions in the methods logic

public class PlayCLI2 {

    private final InMemoryGameRepository repo;
    private final CreateGameUseCase gameUseCase;
    private final PlayHandUseCase playHandUseCase;
    private Intel intel;
    private Player player;
    private final UUID botUUID;
    private final UUID playerUUID;

    public static void main(String[] args) {
        new PlayCLI2();
    }

    public PlayCLI2() {
        repo = new InMemoryGameRepository();
        gameUseCase = new CreateGameUseCase(repo);
        playHandUseCase = new PlayHandUseCase(repo);
        botUUID = UUID.randomUUID();
        playerUUID = UUID.randomUUID();
    }

    private void createGame(){
        player = new CLIPlayer("Lucas", playerUUID);
        gameUseCase.create(player, new MineiroBot(repo, botUUID));
    }

    private void handleCardPlaying(){
        CardReader cardReader = new CardReader(this, null);
        CardModeReader cardModeReader = new CardModeReader(this);
        final CardModeReader.CardMode mode = cardModeReader.execute();
        final Card card = cardReader.execute();

        if(mode == OPEN) intel = playHandUseCase.playCard(playerUUID, card);
        intel = playHandUseCase.discard(playerUUID, card);
    }

    private void handleRaiseRequest(){
        if(intel.isForbidenToRaiseBet() || intel.getHandScore().equals(HandScore.TWELVE)) return;
        RaiseRequestReader requestReader = new RaiseRequestReader(this, intel.getHandScore().increase());
        if(requestReader.execute() == REQUEST) playHandUseCase.raiseBet(playerUUID);
    }

    public void printGameIntel(){
        IntelPrinter intelPrinter  = new IntelPrinter(player, intel, 1000);
        intelPrinter.execute();
    }
}
