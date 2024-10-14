package com.francisco.bruno.pedrohenriquebot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class PedroHenriqueBot implements BotServiceProvider {
    @Override
    public String getName() {
        return "PedroHenrique";
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int manilhas = countManilhas(intel);
        int highCards = countHighCards(intel);

        return (manilhas >= 2 || (manilhas == 1 && highCards >= 2) || highCards == 3);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return true;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return CardToPlay.of(intel.getCards().get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 1;
    }

    public int countManilhas(GameIntel intel){
        return (int) intel.getCards().stream().filter(card -> card.isManilha(intel.getVira())).count();
    }

    private int countHighCards(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return (int) intel.getCards().stream()
                .filter(card -> !card.isManilha(vira))
                .filter(card -> card.getRank() == CardRank.THREE ||
                        card.getRank() == CardRank.TWO ||
                        card.getRank() == CardRank.ACE)
                .count();
    }

    private double handStrengthAverage(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .mapToInt(card -> card.relativeValue(vira))
                .average()
                .orElse(0);
    }
}
