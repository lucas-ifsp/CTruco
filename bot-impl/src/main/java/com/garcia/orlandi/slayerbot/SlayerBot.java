package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import com.garcia.orlandi.slayerbot.SlayerBotUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SlayerBot implements BotServiceProvider {

    SlayerBotUtils utils = new SlayerBotUtils();

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> openCards = intel.getOpenCards();
        List<GameIntel.RoundResult> roundResult = intel.getRoundResults();

        List<TrucoCard> manilhas = utils.getManilhas(cards, vira);

        if(openCards.size() == 1){
            if(!manilhas.isEmpty()){
                List<TrucoCard> cardsWithoutZap = cards.stream()
                        .filter(card -> !card.isZap(vira))
                        .toList();

                if(cardsWithoutZap.size() == 2){
                    for(TrucoCard card : cardsWithoutZap){
                        if(card.isCopas(vira) || card.isEspadilha(vira) || card.isOuros(vira)){
                            return CardToPlay.of(card);
                        }
                    }

                    TrucoCard secondStrongestCard = utils.getStrongestCard(cardsWithoutZap, vira);
                    return CardToPlay.of(secondStrongestCard);
                } else if (cardsWithoutZap.size() == 3) {
                    if(manilhas.size() == 2){
                        TrucoCard weakestManilha = utils.getWeakestCard(manilhas, vira);
                        return CardToPlay.of((weakestManilha));
                    } else if (manilhas.size() == 1) {
                        TrucoCard strongestIsManilha = utils.getStrongestCard(cards, vira);
                        List<TrucoCard> cardsWithoutManilha = new ArrayList<>(cards);
                        cardsWithoutManilha.remove(strongestIsManilha);
                        TrucoCard strongestCardExceptManilha = utils.getStrongestCard(cardsWithoutManilha, vira);
                        return CardToPlay.of(strongestCardExceptManilha);
                    }
                }

                TrucoCard chosenCard = cards.stream()
                        .filter(card -> !card.isZap(vira))
                        .findFirst()
                        .orElse(null);

                return CardToPlay.of(chosenCard);
            }
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
