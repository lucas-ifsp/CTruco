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

        return handStrength(intel) > 25 && hasManilha(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        TrucoCard weakestCard = getWeakestCard(intel);
        TrucoCard strongestCard = getStrongestCard(intel);

        if(wonFirstRound(intel) && canKill(intel, weakestCard)) return true;

        if(wonFirstRound(intel) && canKill(intel, strongestCard)) return true;

        if(hasCopas(intel) && hasZap(intel)) return true;

        if(wonFirstRound(intel) && hasZap(intel)) return true;

        if(wonFirstRound(intel) && hasCopas(intel) && isZapAlreadyUsed(intel)) return true;

        if(handStrength(intel) > 25 && intel.getOpponentScore() < 4) return true;

        return handStrength(intel) > 27 && hasManilha(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if (intel.getOpponentCard().isPresent()) return CardToPlay.of(chooseResponseCard(intel));

        int roundsPlayed = intel.getRoundResults().size();

        if (roundsPlayed == 0) return playFirstRound(intel);

        else if (roundsPlayed == 1) return playSecondRound(intel);

        else return CardToPlay.of(getStrongestCard(intel));

    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        double strength = handStrength(intel);

        if (wonFirstRound(intel) && canKill(intel, getWeakestCard(intel))) return 1;

        if (strength > 27 && (hasZap(intel) || hasCopas(intel))) return 1;

        if (wonFirstRound(intel) && hasCopas(intel) && isZapAlreadyUsed(intel)) return 1;

        if (strength >= 22 && strength <= 26 && hasManilha(intel)) return 1;

        return -1;
    }

    private CardToPlay playFirstRound(GameIntel intel){

        if (intel.getOpponentCard().isPresent()) return CardToPlay.of(chooseResponseCard(intel));

        double strength = handStrength(intel);

        Optional<TrucoCard> strongestCardWithoutManilha = getStrongestCardWithoutManilha(intel);
        if(strongestCardWithoutManilha.isPresent()){
            if (strength <= 10) return CardToPlay.of(strongestCardWithoutManilha.get());

            Optional<TrucoCard> midCard = getMidCard(intel);

            return midCard.map(CardToPlay::of)
                    .orElse(CardToPlay.of(strongestCardWithoutManilha.get()));
        }

        return CardToPlay.of(strongestCardWithoutManilha.get());

    }

    private CardToPlay playSecondRound(GameIntel intel) {
        boolean wonFirstRound = wonFirstRound(intel);
        if (wonFirstRound) return CardToPlay.of(getWeakestCard(intel));

        Optional<TrucoCard> strongestCardWithoutManilha = getStrongestCardWithoutManilha(intel);
        double strength = handStrength(intel);
        if(strongestCardWithoutManilha.isPresent())
            if (strength > 14 || (strength <= 14 && hasManilha(intel))) return CardToPlay.of(strongestCardWithoutManilha.get());

        return getMidCard(intel).map(CardToPlay::of)
                .orElse(CardToPlay.of(getStrongestCard(intel)));
    }

    private TrucoCard chooseResponseCard(GameIntel intel) {
        TrucoCard opponentCard = intel.getOpponentCard().get();
        int opponentCardValue = opponentCard.relativeValue(intel.getVira());

        TrucoCard responseCard = getWeakestCard(intel);
        int bestCardValue = Integer.MAX_VALUE;

        int highestOpenCardValue = intel.getOpenCards().stream()
                .mapToInt(card -> card.relativeValue(intel.getVira()))
                .max()
                .orElse(-1);

        for (TrucoCard card : intel.getCards()) {
            int cardValue = card.relativeValue(intel.getVira());

            if (cardValue > opponentCardValue && cardValue > highestOpenCardValue && cardValue < bestCardValue) {
                bestCardValue = cardValue;
                responseCard = card;
            }
        }

        if (responseCard == getWeakestCard(intel)) {
            return getMidCard(intel).orElse(getWeakestCard(intel));
        }

        return responseCard;
    }



    ///////////////////////////////////////////////
    //Non Required methods
    ///////////////////////////////////////////////



    private boolean hasManilha(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isManilha(intel.getVira()));
    }

  private boolean hasZap(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isZap(intel.getVira()));
    }

    private boolean hasCopas(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isCopas(intel.getVira()));
    }

    private boolean hasEspadilha(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isEspadilha(intel.getVira()));
    }

    private boolean hasOuros(GameIntel intel) {
        return intel.getCards()
                .stream()
                .anyMatch(e -> e.isOuros(intel.getVira()));
    }

    private double handStrength(GameIntel intel) {
        return intel.getCards().stream()
                .mapToDouble(e -> e.relativeValue(intel.getVira()))
                .sum();
    }

    private TrucoCard getManilhaCard(GameIntel intel) {
        return intel.getCards()
                .stream()
                .filter(card -> card.isManilha(intel.getVira()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No manilha card found"));
    }

    private TrucoCard getStrongestCard(GameIntel intel) {
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

    private Optional<TrucoCard> getStrongestCardWithoutManilha(GameIntel intel) {
        return intel.getCards().stream()
                .filter(e -> !e.isManilha(intel.getVira()))
                .max( (card1, card2) -> card1.compareValueTo(card2, intel.getVira()) );
    }

    private TrucoCard getWeakestCard(GameIntel intel) {
        return intel.getCards().stream()
                .min((card1, card2) -> card1.compareValueTo(card2, intel.getVira()))
                .orElseThrow(() -> new NullPointerException("There is no Cards"));
    }

    private Optional<TrucoCard> getMidCard(GameIntel intel) {
        return intel.getCards().stream()
                .filter(e -> !e.equals(getStrongestCard(intel)) && !e.equals(getWeakestCard(intel)) )
                .findFirst();
    }

    private boolean wonFirstRound(GameIntel intel) {
        if(intel.getRoundResults().isEmpty()) return false;

        return intel.getRoundResults().get(0).equals(GameIntel.RoundResult.WON);
    }

    private boolean canKill(GameIntel intel, TrucoCard card) {
        if(intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            return card.compareValueTo(opponentCard, intel.getVira()) > 0;
        }

        return false;
    }

    private boolean isZapAlreadyUsed(GameIntel intel) {
        Optional<TrucoCard> maybeZap= intel.getOpenCards()
                .stream()
                .filter(e -> e.isZap(intel.getVira()))
                .findFirst();

        return maybeZap.isPresent();
    }

}