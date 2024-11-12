package com.abel.francisco.fogao6boca;

import com.brito.macena.boteco.utils.Game;
import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.GameIntel.RoundResult.WON;

public class Fogao6Boca implements BotServiceProvider {


    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(casalMaior(intel)) return true;
        if(manilhas(intel) >= 2) return true;
        return verifyElevenHandStrengh(intel) > 1;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(casalMaior(intel)) return true;
        if(manilhas(intel) >= 2) return true;
        if (!intel.getRoundResults().isEmpty() && intel.getRoundResults().size() > 1) {
            if (intel.getRoundResults().get(0) == WON || intel.getRoundResults().get(1) == WON) return true;
        }
        return verifyHandStrengh(intel) > 6;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return gameRound(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(casalMaior(intel)) return 1;
        if(manilhas(intel) >= 2) return 1;
        if(!intel.getRoundResults().isEmpty() && intel.getRoundResults().get(0) == WON)
            if(verifyHandStrengh(intel) > 6) return 0;
        return -1;
    }


    private CardToPlay gameRound(GameIntel intel){
        List<TrucoCard> botCards = sortedCardStrengh(intel.getCards(),intel.getVira());
        return decideCardToPlay(botCards, intel.getOpponentCard(), intel.getVira(), intel);
    }

    private List<TrucoCard> sortedCardStrengh(List<TrucoCard> cards, TrucoCard vira){
        List<TrucoCard> sortedCards = new ArrayList<>(cards);
        sortedCards.sort((card1, card2) -> card2.compareValueTo(card1,vira));
        return sortedCards;
    }

    private CardToPlay decideCardToPlay(List<TrucoCard> cards, Optional<TrucoCard> oponentCard, TrucoCard vira, GameIntel intel){
        if(oponentCard.isPresent()){
            List<TrucoCard> possibleCards = new ArrayList<>();
            for(TrucoCard card : cards){
                if(card.compareValueTo(oponentCard.get(), vira) > 0)
                    possibleCards.add(card);
            }
            if(!possibleCards.isEmpty()) return CardToPlay.of(possibleCards.get(possibleCards.size()-1));

            for(TrucoCard card : cards){
                if(card.compareValueTo(oponentCard.get(), vira) == 0)
                    return CardToPlay.of(card);
            }
            return CardToPlay.of(cards.get(cards.size()-1));
        }
        if(intel.getRoundResults().size() == 1 && intel.getRoundResults().get(0) == WON){
            return CardToPlay.of(cards.get(cards.size()-1));
        }
        return CardToPlay.of(cards.get(0));
    }

    private double verifyHandStrengh(GameIntel intel){
        double soma = 0;
        for(TrucoCard card : intel.getCards()){
            soma += card.relativeValue(intel.getVira());
        }
        return soma/intel.getCards().size();

    }

    private double verifyElevenHandStrengh(GameIntel intel){
        List<TrucoCard> possibleCards = new ArrayList<>();
        for(TrucoCard card : intel.getCards())
            if(card.relativeValue(intel.getVira()) > 6) possibleCards.add(card);
        return possibleCards.size();
    }

    private int manilhas(GameIntel intel){
        List<TrucoCard> cards = intel.getCards();
        int qtdManilhas = 0;
        for(TrucoCard card : cards){
            if(card.isManilha(intel.getVira())){
                qtdManilhas++;
            }
        }
        return qtdManilhas;
    }


    public boolean casalMaior(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        boolean zap = false;
        for(TrucoCard card : cards){
            if(card.isZap(intel.getVira())){
                zap = true;
                break;
            }
        }
        boolean copas = false;
        for(TrucoCard card : cards){
            if(card.isCopas(intel.getVira())){
                copas = true;
                break;
            }
        }
        return zap && copas;
    }
}