package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class TiaoDoTruco implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(hasZap(intel) && hasCopas(intel)) return true;

        if(intel.getOpponentScore() < 4 && handStrength(intel) > 26) return true;

        if(intel.getOpponentScore() < 5 && handStrength(intel) > 18 && hasManilha(intel)) return true;

        return handStrength(intel) > 35 && hasManilha(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if(hasCopas(intel) && hasZap(intel)) return true;

        if(wonFirstRound(intel) && hasZap(intel)) return true;

        if(wonFirstRound(intel) && hasCopas(intel)) return true;

        if(handStrength(intel) > 25 && intel.getOpponentScore() < 4) return true;

        return handStrength(intel) > 35 && hasManilha(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard weakestCard = getWeakestCard(intel);
        TrucoCard strongestCard = getStrongestCard(intel);

        if (canKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

        if ( getMidCard(intel).isPresent() && canKill(intel, getMidCard(intel).get()) ) return CardToPlay.of(getMidCard(intel).get());

        if (hasZap(intel) && hasCopas(intel)) {
            return weakestCard != null ? CardToPlay.of(weakestCard) : null;
        }

        if (hasZap(intel)) {
            return weakestCard != null ? CardToPlay.of(weakestCard) : null;
        }

        if (handStrength(intel) > 35) {
            return strongestCard != null ? CardToPlay.of(strongestCard) : null;
        }

        return weakestCard != null ? CardToPlay.of(weakestCard) : null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(handStrength(intel) > 27 && hasZap(intel) ) return 1;

        if(hasZap(intel)) return 1;

        if(handStrength(intel) > 25 && hasManilha(intel)) return 0;

        return -1;
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

    public boolean hasEspadilha(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isEspadilha(intel.getVira()));
    }

    public boolean hasOuros(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isOuros(intel.getVira()));
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

    public TrucoCard getStrongestCard(GameIntel intel) {
        if(hasZap(intel)) return intel.getCards().stream()
                .filter(e -> e.isZap(intel.getVira()))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No such element"));

        if(hasCopas(intel)) return intel.getCards().stream()
                .filter(e -> e.isCopas(intel.getVira()))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No such element"));

        if(hasEspadilha(intel)) return intel.getCards().stream()
                .filter(e -> e.isEspadilha(intel.getVira()))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No such element"));

        if(hasOuros(intel)) return intel.getCards().stream()
                .filter(e -> e.isOuros(intel.getVira()))
                .findFirst()
                .orElseThrow(() -> new NullPointerException("No such element"));

        return intel.getCards().stream()
                .max((card1, card2) -> card1.compareValueTo(card2, intel.getVira()))
                .orElseThrow( () -> new NullPointerException("There is no Cards") );
    }

    public TrucoCard getWeakestCard(GameIntel intel) {
        return intel.getCards().stream()
                .min((card1, card2) -> card1.compareValueTo(card2, intel.getVira()))
                .orElseThrow(() -> new NullPointerException("There is no Cards"));
    }

    public Optional<TrucoCard> getMidCard(GameIntel intel) {
        return intel.getCards().stream()
                .filter(e -> !e.equals(getStrongestCard(intel)) && !e.equals(getWeakestCard(intel)) )
                .findFirst();
    }

    public boolean wonFirstRound(GameIntel intel) {
        if(intel.getRoundResults().isEmpty()) return false;

        return intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON);
    }

    public boolean canKill(GameIntel intel, TrucoCard card) {
        if(intel.getOpponentCard().isPresent())
            return card.compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0;

        return false;
    }
}