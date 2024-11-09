package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.service.BotServiceProvider;

public class TiaoDoTruco implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(hasZap(intel) && hasCopas(intel)) return true;

        if(intel.getOpponentScore() < 4 && handStrength(intel) > 26) return true;

        if(intel.getOpponentScore() < 5 && handStrength(intel) > 18 && hasManilha(intel)) return true;

        return handStrength(intel) > 35;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if(hasCopas(intel) && hasZap(intel)) return true;

        if(handStrength(intel) > 25 && intel.getOpponentScore() < 4) return true;

        return handStrength(intel) > 35;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    ///////////////////////////////////////////////
    //Non Required methods
    ///////////////////////////////////////////////

    public boolean hasManilha(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isManilha(intel.getVira()));
    }

    public boolean hasZap(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isZap(intel.getVira()));
    }

    public boolean hasCopas(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isCopas(intel.getVira()));
    }

    public double handStrength(GameIntel intel) {
        return intel.getCards().stream()
                .mapToDouble(e -> {
                    double strenght = 0;
                    if( e.isZap(intel.getVira()) ) return 20;
                    if( e.isCopas(intel.getVira()) ) return 15;
                    if( e.isEspadilha(intel.getVira()) ) return 13;
                    if( e.isOuros(intel.getVira()) ) return 12;

                    return switch (e.getRank()) {
                        case THREE -> CardRank.THREE.value();
                        case TWO -> CardRank.TWO.value();
                        case ACE -> CardRank.ACE.value();
                        case KING -> CardRank.KING.value();
                        case JACK -> CardRank.JACK.value();
                        case QUEEN -> CardRank.QUEEN.value();
                        case SEVEN -> CardRank.SEVEN.value();
                        case SIX -> CardRank.SIX.value();
                        case FIVE -> CardRank.FIVE.value();
                        case FOUR -> CardRank.FOUR.value();
                        default -> 0;
                    };
                })
                .sum();
    }
}