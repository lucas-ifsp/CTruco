package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseStrategy implements BotServiceProvider {
    boolean trucar;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(TiaoDoTruco.hasManilha(intel) && TiaoDoTruco.hasThree(intel) && intel.getOpponentScore() < 8) return true;

        if(handStrength(intel) > 20 && intel.getOpponentScore() < 4) return true;

        return handStrength(intel) > 25;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(trucar) return true;

        if(intel.getRoundResults().isEmpty()) return false;

        if(TiaoDoTruco.firstRoundWon(intel)) {
            if(!TiaoDoTruco.canKill(intel) && intel.getOpponentScore() > 7) return false;

            return TiaoDoTruco.canKill(intel) || !(handStrength(intel) < 14);
        }

        return true;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        trucar = false;
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);
        Optional<TrucoCard> midCard = TiaoDoTruco.getMidCard(intel);
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);

        if(intel.getRoundResults().isEmpty() && intel.getOpponentCard().isPresent()){
            TrucoCard opponentCard = intel.getOpponentCard().get();

            Optional<TrucoCard> drawCard = intel.getCards().stream()
                    .filter(e -> e.compareValueTo(opponentCard, intel.getVira()) == 0)
                    .findFirst();

            if(TiaoDoTruco.hasThree(intel) && drawCard.isPresent()) {
                if (drawCard.get().compareValueTo(TrucoCard.of(CardRank.THREE, CardSuit.CLUBS), intel.getVira()) != 0) {
                    trucar = true;
                    return CardToPlay.of(drawCard.get());
                }

                if(TiaoDoTruco.canKill(intel)) {
                    if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

                    return CardToPlay.of(drawCard.orElse(strongestCard));
                }
            }
        }

        if(TiaoDoTruco.canKill(intel)) {
            if(TiaoDoTruco.cardCanKill(intel, weakestCard)) return CardToPlay.of(weakestCard);

            return CardToPlay.of(midCard.orElse(strongestCard));
        }

        return CardToPlay.of(weakestCard);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(TiaoDoTruco.hasWonFirstHand(intel) && TiaoDoTruco.canKill(intel)) return 1;

        if(TiaoDoTruco.hasWonFirstHand(intel) && hasManilha(intel) && intel.getOpponentScore() < 8) return 1;

        if(TiaoDoTruco.hasWonFirstHand(intel)) return 0;

        return -1;
    }

    ///////////////////////////////////////////////
    //Non Required methods
    ///////////////////////////////////////////////


    private Set<TrucoCard> hasBiggerThanTwo(GameIntel intel) {
        return intel.getCards().stream()
                .filter(e -> e.getRank().value() > 9)
                .collect(Collectors.toSet());
    }

    public int countManilha(GameIntel intel) {
        return intel.getCards().stream()
                .filter(e -> e.isManilha(intel.getVira()))
                .collect(Collectors.toSet())
                .size();
    }

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
                .mapToDouble(e -> e.relativeValue(intel.getVira()))
                .sum();
    }

    public boolean isZapAlreadyUsed(GameIntel intel) {
        Optional<TrucoCard> maybeZap= intel.getOpenCards()
                .stream()
                .filter(e -> e.isZap(intel.getVira()))
                .findFirst();

        return maybeZap.isPresent();
    }

}