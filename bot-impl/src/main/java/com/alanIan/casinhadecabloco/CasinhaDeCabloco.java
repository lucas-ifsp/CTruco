package com.alanIan.casinhadecabloco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class CasinhaDeCabloco implements BotServiceProvider {

    public int CardsValues(TrucoCard vira, List<TrucoCard> cards){
        return cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();
    }

    public boolean getMaoDeOnzeResponse(GameIntel intel){

        final List<TrucoCard> BotCards = intel.getCards();
        TrucoCard vira = intel.getVira();
        int EnemyScore = intel.getOpponentScore();
        int value = CardsValues(vira, BotCards);

        if (EnemyScore >= 9 && EnemyScore < 11){
            if (value >= 25){
                return true;
            } else{
                return false;
            }
        }else{
            return true;
        }
    }

    /**
     * <p>Decides if the bot wants to request a hand points raise.</p>
     * @return {@code true} if it wants to request a hand points raise or {@code false} otherwise.
     */
    public boolean decideIfRaises(GameIntel intel) {
        final List<TrucoCard> BotCards = intel.getCards();
        TrucoCard vira = intel.getVira();
        int value = CardsValues(vira, BotCards);
        int EnemyScore = intel.getOpponentScore();
        int BotScore = intel.getScore();
        int round = intel.getRoundResults().size() + 1;

        boolean conservative = false;
        boolean aggressive = true;

        if (BotScore >= 9 || EnemyScore >= 9) {
            if (value >= 20) return aggressive;
        }

        if (round == 2) {
            if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
                if (value >= 25) return aggressive;
                return conservative;
            } else if (intel.getRoundResults().get(0) == GameIntel.RoundResult.LOST) {
                if (value >= 22) return aggressive;
            }
        }

        if (BotScore < EnemyScore) {
            if (EnemyScore == 10) return aggressive;
            if (value >= 25) return aggressive;
        }

        if (BotScore > EnemyScore) {
            if (BotScore >= 10) return conservative;
        }

        return conservative;
    }

    /**
     * <p>Choose a card to be played or discarded. The card is represented by a {@link CardToPlay} object,
     * which wraps a {@link TrucoCard} and adds information about whether it should be played or discarded.</p>
     * @return a TrucoCard representing the card to be played or discarded.
     */
    public CardToPlay chooseCard(GameIntel intel){
        TrucoCard card = null;
        return CardToPlay.discard(card); // Apenas para possibilitar os testes
    }

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
    public int getRaiseResponse(GameIntel intel){
        return 1; // Apenas para possibilitar os testes
    }


    /**
     * <p>Returns the bot name. By default, the bot name is the name of the class implementing this interface.</p>
     * @return The bot name that will be used during the game.
     */
    public String getName(){
        return "Casinha de Caboclo";
    }

}
