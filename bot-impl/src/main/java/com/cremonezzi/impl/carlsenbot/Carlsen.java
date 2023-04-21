package com.cremonezzi.impl.carlsenbot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

/* Notes
*
* CARDS RANK ORDER
*  4 - 5 - 6 - 7 - Q - J - K - A - 2 - 3
*
*  40 Cards in the deck (each rank has all the 4 suits => 10 ranks x 4 suits)
*
*  CARDS SUIT ORDER
*  Diamond - Spade - Heart - Club
*
* */

public class Carlsen implements BotServiceProvider {
    @Override
    public int getRaiseResponse(GameIntel intel) {
        int qntManilhas = manilhas(intel.getCards(), intel.getVira()).size();
        List<TrucoCard> hand = intel.getCards();
        List<GameIntel.RoundResult> roundResult = intel.getRoundResults();
        int highCard = 0;

        for (TrucoCard card : hand){
            if(card.equals(CardRank.ACE) || card.equals((CardRank.TWO)) || card.equals(CardRank.THREE)) {
                highCard++;
            }
        }

        if (qntManilhas == 0) {
            return -1;
        }

        if (qntManilhas == 1) {
            if(highCard >= 1 || roundResult.contains(GameIntel.RoundResult.WON)) {
                return 1;
            }
            return 0;
        }

        return 1;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        int haveZap = haveZap(intel.getCards(), vira);

        return haveZap > -1;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (intel.getOpponentCard().isPresent() && intel.getOpponentCard().get().getRank().equals(CardRank.HIDDEN)) {
            return CardToPlay.of(lowerInHand(intel.getCards(), intel.getVira()));
        }

        if (intel.getRoundResults().size() > 0 && intel.getRoundResults().get(0).equals(GameIntel.RoundResult.LOST)) {
            return CardToPlay.of(higherInHand(intel.getCards(), intel.getVira()));
        }

        if (intel.getOpponentCard().isEmpty()) {
            return CardToPlay.of(lowerInHand(intel.getCards(), intel.getVira()));
        }

        TrucoCard opponentCard = intel.getOpponentCard().get();
        if (opponentCard.isZap(intel.getVira())) {
            if (intel.getRoundResults().isEmpty()) {
                return CardToPlay.of(lowerInHand(intel.getCards(), intel.getVira()));
            }

            return CardToPlay.discard(lowerInHand(intel.getCards(), intel.getVira()));
        }

        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public String getName() {
        return "Trucus Carlsen";
    }

    private int haveZap(List<TrucoCard> botCards, TrucoCard vira) {
        Optional<TrucoCard> haveZap = botCards.stream().filter(trucoCard -> trucoCard.isZap(vira)).findFirst();

        return haveZap.map(botCards::indexOf).orElse(-1);
    }

    private TrucoCard lowerInHand(List<TrucoCard> botCards, TrucoCard vira) {
        TrucoCard lower = botCards.get(0);

        for (TrucoCard trucoCard : botCards) {
            if (trucoCard.relativeValue(vira) < lower.relativeValue(vira)) {
                lower = trucoCard;
            }
        }

        return lower;
    }

    private TrucoCard higherInHand(List<TrucoCard> botCards, TrucoCard vira) {
        TrucoCard higher = botCards.get(0);

        for (TrucoCard trucoCard : botCards) {
            if (trucoCard.relativeValue(vira) > higher.relativeValue(vira)) {
                higher = trucoCard;
            }
        }

        return higher;
    }

    private List<TrucoCard> manilhas(List<TrucoCard> botCards, TrucoCard vira) {
        return botCards.stream().filter(trucoCard -> trucoCard.isManilha(vira)).toList();
    }
}
