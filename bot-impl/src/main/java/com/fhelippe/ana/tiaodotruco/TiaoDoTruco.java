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

        if(hasCasalPreto(intel)) strategy = new BlackCouple();

        if(getHandStrength(intel) > 30) strategy = new StrengthHandWithManilha();

        if(getHandStrength(intel) > 20) strategy = new StrengthHand();

        if(getHandStrength(intel) > 10) strategy = new BaseStrategy();

        else strategy = new WeakyHand();

        return strategy;
    }

    //valores sem contabilizar manilha:
    //mao forte: 20 - 30
    //mao media: 10 - 20
    //mao fraca: 0 - 10
    private int getHandStrength(GameIntel intel) {
        int value = intel.getCards().stream()
                .mapToInt(e -> e.getRank().value())
                .sum();

        if(TiaoDoTruco.hasZap(intel)) value += 10;

        if(TiaoDoTruco.hasCopas(intel)) value += 10;

        if(TiaoDoTruco.hasEspadilha(intel)) value += 8;

        if(TiaoDoTruco.hasOuros(intel)) value += 5;

        return value;
    }

    static boolean lostFirstRoundWithManilha(GameIntel intel) {
        if(intel.getRoundResults().isEmpty()) return false;

        TrucoCard card1 = intel.getOpenCards().get(1);
        TrucoCard card2 = intel.getOpenCards().get(2);

        return card1.isManilha(intel.getVira()) && card2.isManilha(intel.getVira()) && !TiaoDoTruco.hasWonFirstHand(intel);
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

    static protected boolean hasEspadilha(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.isEspadilha(intel.getVira()));
    }

    static protected boolean hasOuros(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(e -> e.isOuros(intel.getVira()));
    }
}