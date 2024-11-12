package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;

public class TiaoDoTruco implements BotServiceProvider {
    BotServiceProvider strategy;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        strategy = selectStrategy(intel);

        return strategy.getMaoDeOnzeResponse(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        strategy = selectStrategy(intel);

        return strategy.decideIfRaises(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        strategy = selectStrategy(intel);

        return strategy.chooseCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        strategy = selectStrategy(intel);

        return strategy.getRaiseResponse(intel);
    }

    ///////////////////////////////////////////////
    //Non Required methods
    ///////////////////////////////////////////strategy////

    private BotServiceProvider selectStrategy(GameIntel intel) {
        if(hasZap(intel) && hasCopas(intel)) strategy = new ZapCopas();
//
//        if(hasCasalPreto(intel)) strategy = new BlackCouple();
//
//        if(hasTwoManilha(intel) && hasGreatCard(intel)) strategy = new TwoManilhaAndStrongCard();
//
//        if(getHandStrength(intel) > 27 && !hasManilha(intel)) strategy = new ThreeGoodCards();
//
//        if(getHandAverage(intel) <= 4) strategy = new WeakyHand();
//
//        if(hasThree(intel) && hasTwo(intel) && hasManilha(intel)) return new ThreeComaTwoAndManilha();
//
//        if((hasZap(intel) || hasCopas(intel)) && hasGreatCard(intel)) new BaseStrategy();

        //estrategia para veificar se tem 3 ou 2 e outra carta forte
        strategy = new BaseStrategy();

        return strategy;
    }

    static boolean lostFirstRoundWithManilha(GameIntel intel) {
        if(intel.getRoundResults().isEmpty()) return false;

        TrucoCard card1 = intel.getOpenCards().get(1);
        TrucoCard card2 = intel.getOpenCards().get(2);

        return card1.isManilha(intel.getVira()) && card2.isManilha(intel.getVira()) && !TiaoDoTruco.hasWonFirstHand(intel);
    }

    private int getHandStrength(GameIntel intel) {
        return intel.getCards().stream()
                .mapToInt(e -> e.getRank().value())
                .sum();
    }

    static protected boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.isManilha(intel.getVira()));
    }

    static protected boolean hasCasalPreto(GameIntel intel) {
        return intel.getCards().stream()
                .filter(e -> e.isZap(intel.getVira()) || e.isEspadilha(intel.getVira()))
                .count() == 2;
    }

    static protected boolean hasThree(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.getRank().value() == 10);
    }

    static protected boolean hasTwo(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.getRank().value() == 9);
    }

    static protected boolean hasWonFirstHand(GameIntel intel) {
        return !intel.getRoundResults().isEmpty() && GameIntel.RoundResult.WON.equals(intel.getRoundResults().get(0));
    }

    static protected boolean cardCanKill(GameIntel intel, TrucoCard card) {
        if(intel.getOpponentCard().isEmpty()) return false;

        TrucoCard opponentCard = intel.getOpponentCard().get();
        return card.compareValueTo(opponentCard, intel.getVira()) > 1 ||
                (hasWonFirstHand(intel) && card.compareValueTo(opponentCard, intel.getVira()) == 0);
    }

    static protected double getHandAverage(GameIntel intel) {
        return intel.getCards().stream()
                .mapToInt(e -> e.relativeValue(intel.getVira()))
                .average()
                .orElse(0);
    }

    static protected boolean firstRoundWon(GameIntel intel) {
        if(intel.getRoundResults().isEmpty()) return false;

        return intel.getRoundResults().get(0) == GameIntel.RoundResult.WON;
    }

    static protected boolean hasTwoManilha(GameIntel intel) {
        return intel.getCards().stream()
                .filter(e -> e.isManilha(intel.getVira()))
                .count() > 2;
    }

    static protected boolean canKill(GameIntel intel) {
        if(intel.getOpponentCard().isEmpty()) return false;

        TrucoCard opponentCard = intel.getOpponentCard().get();
        return intel.getCards().stream()
                .anyMatch(e -> e.compareValueTo(opponentCard, intel.getVira()) > 0);
    }

    static protected TrucoCard getStrongestCard(GameIntel intel) {
        return intel.getCards().stream()
                .max((card1, card2) -> card1.compareValueTo(card2, intel.getVira()))
                .orElseThrow(() -> new IllegalArgumentException("No such element on ZapCopas getStrongestCard"));
    }

    static protected Optional<TrucoCard> getMidCard(GameIntel intel) {
        TrucoCard strongestCard = TiaoDoTruco.getStrongestCard(intel);
        TrucoCard weakestCard = TiaoDoTruco.getWeakestCard(intel);

        return intel.getCards().stream()
                .filter(e -> !e.equals(strongestCard) && !e.equals(weakestCard))
                .findFirst();
    }

    static protected TrucoCard getWeakestCard(GameIntel intel) {
        return intel.getCards().stream()
                .min((card1, card2) -> card1.compareValueTo(card2, intel.getVira()))
                .orElseThrow(() -> new IllegalArgumentException("No such element on ZapCopas getWeakestCard"));
    }

    static protected boolean hasGreatCard(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.getRank().value() == 10 || e.getRank().value() == 9
                || e.getRank().value() == 8);
    }

    static protected boolean hasZap(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.isZap(intel.getVira()));
    }

    static protected boolean hasCopas(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.isCopas(intel.getVira()));
    }
}