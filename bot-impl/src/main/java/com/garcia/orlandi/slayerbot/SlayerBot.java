package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;
import com.garcia.orlandi.slayerbot.SlayerBotUtils.*;

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

        if(!manilhas.isEmpty()){
            if(openCards.size() == 1){
                List<TrucoCard> cardsWithoutZap = cards.stream()
                        .filter(card -> !card.isZap(vira))
                        .toList();

                for(TrucoCard card : cardsWithoutZap){
                    if(card.isCopas(vira)){
                        return CardToPlay.of(card);
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
