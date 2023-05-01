/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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

package com.bueno.spi.service;


import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

/**
 * <p>This interface imposes the behaviour that any bot must provide in order to enroll in a truco game.</p>
 * */
public interface BotServiceProvider{

    /**
     * <p>Decides if the bot wants to play the "mão de onze". If it decides to play, the hand points will be
     * increased to 3. Otherwise, the bot loses the current hand and the opponent receives 1 point.</p>
     * @return {@code true} if it accepts to play the hand or {@code false} if it quits.
     */
    boolean getMaoDeOnzeResponse(GameIntel intel);

    /**
     * <p>Decides if the bot wants to request a hand points raise.</p>
     * @return {@code true} if it wants to request a hand points raise or {@code false} otherwise.
     */
    boolean decideIfRaises(GameIntel intel);

    /**
     * <p>Choose a card to be played or discarded. The card is represented by a {@link CardToPlay} object,
     * which wraps a {@link TrucoCard} and adds information about whether it should be played or discarded.</p>
     * @return a TrucoCard representing the card to be played or discarded.
     */
    CardToPlay chooseCard(GameIntel intel);

    /**
     * <p>Decides what the bot does when the opponent requests to increase the hand points. If the bot decides to
     * quit, it loses the hand. If it decides to accept, the hand points will be increased and the game continues.
     * If it decides to re-raise, the hand points will be increased and a higher bet will be placed to the bot
     * opponent. If the current hand points request is already enough to the losing player to win and the bot decides
     * to re-raise, the decision will be considered as acceptance and no request will be made to the opponent.</p>
     *
     * @return {@code -1} if the bot quits, {@code 0} if it accepts, and {@code 1} if bot wants to place a re-raise
     * request.
     */
    int getRaiseResponse(GameIntel intel);


    /**
     * <p>Returns the bot name. By default, the bot name is the name of the class implementing this interface.</p>
     * @return The bot name that will be used during the game.
     */
    default String getName(){
        return getClass().getSimpleName();
    }
}
