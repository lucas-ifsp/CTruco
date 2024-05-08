package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;


public class SlayerBot implements BotServiceProvider {

    SlayerBotUtils utils = new SlayerBotUtils();

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        List<TrucoCard> manilhas = utils.getManilhas(cards, vira);
        List<TrucoCard> threes = utils.getThreesAtHand(cards);

        if(manilhas.isEmpty()){
            return false;
        }else if(manilhas.size() == 2 || !threes.isEmpty()){
            return true;
        }

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
        if(openCards.size() == 1) {
            //Play second strongest card if in first round or if lost first round
            if (roundResult.isEmpty()) {
                TrucoCard strongestCard = utils.getStrongestCard(cards, vira);
                TrucoCard secondStrongestCard = utils.getSecondStrongestCard(cards, strongestCard, vira);
                return CardToPlay.of(secondStrongestCard);
            } else  {
                for (TrucoCard card : cards) {
                    //Testing in order to play best manilha first, if found
                    if (card.isZap(vira) || card.isCopas(vira) || card.isEspadilha(vira) || card.isOuros(vira)) {
                        return CardToPlay.of(card);
                    }
                }
                //If has no manilha, returns the strongest card
                return CardToPlay.of(utils.getStrongestCard(cards, vira));
            }
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
}
