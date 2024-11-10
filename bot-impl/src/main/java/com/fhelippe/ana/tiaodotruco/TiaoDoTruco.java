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
        if (intel.getOpponentCard().isPresent()) {
            return CardToPlay.of(chooseResponseCard(intel));
        } else if (intel.getRoundResults().isEmpty()) {
            return playFirstRound(intel);
        } else if (intel.getRoundResults().size() == 1) {
            return playSecondRound(intel);
        } else {
            return CardToPlay.of(getStrongestCard(intel));
        }
    }

    private CardToPlay playFirstRound(GameIntel intel) {
        if (intel.getOpponentCard().isPresent()) {
            return CardToPlay.of(chooseResponseCard(intel));
        }
        if (handStrength(intel) <= 9 && hasManilha(intel)) {
            return CardToPlay.of(getManilhaCard(intel));
        }

        Optional<TrucoCard> midCard = getMidCard(intel);
        return midCard.map(CardToPlay::of).orElseGet(() -> CardToPlay.of(getStrongestCard(intel)));

    }


    private CardToPlay playSecondRound(GameIntel intel) {
        if (intel.getOpponentCard().isPresent()) {
            return CardToPlay.of(chooseResponseCard(intel));
        }

        boolean wonFirstRound = wonFirstRound(intel);

        if (wonFirstRound) {
            return CardToPlay.of(getWeakestCard(intel));
        }

        if (!wonFirstRound && handStrength(intel) > 14) {
            return CardToPlay.of(getStrongestCard(intel));
        }

        if (handStrength(intel) <= 14 && hasManilha(intel)) {
            return CardToPlay.of(getManilhaCard(intel));
        }

        Optional<TrucoCard> midCard = getMidCard(intel);
        return midCard.map(CardToPlay::of).orElseGet(() -> CardToPlay.of(getStrongestCard(intel)));

    }


    private TrucoCard chooseResponseCard(GameIntel intel) {
        Optional<TrucoCard> opponentCardOpt = intel.getOpponentCard();
        if (opponentCardOpt.isPresent()) {
            TrucoCard opponentCard = opponentCardOpt.get();
            int opponentCardValue = opponentCard.relativeValue(intel.getVira());

            TrucoCard responseCard = getWeakestCard(intel);

            int bestCardValue = Integer.MAX_VALUE;

            for (TrucoCard card : intel.getCards()) {
                int cardValue = card.relativeValue(intel.getVira());

                if (cardValue > opponentCardValue && cardValue < bestCardValue) {
                    bestCardValue = cardValue;
                    responseCard = card;
                }
            }

            if (responseCard == getWeakestCard(intel)) {
                Optional<TrucoCard> midCard = getMidCard(intel);
                if (midCard.isPresent()) {
                    return midCard.get();
                }
            }
            return responseCard;
        }

        return getWeakestCard(intel);
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {

        if(wonFirstRound(intel) && canKill(intel, getWeakestCard(intel))) return 1;

        if(handStrength(intel) > 25 && hasZap(intel) ) return 1;

        if(wonFirstRound(intel) && hasCopas(intel) && isZapAlreadyUsed(intel)) return 1;

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
        if(intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            return card.compareValueTo(opponentCard, intel.getVira()) > 0;
        }

        return false;
    }

    public boolean isZapAlreadyUsed(GameIntel intel) {
        Optional<TrucoCard> maybeZap= intel.getOpenCards()
                .stream()
                .filter(e -> e.isZap(intel.getVira()))
                .findFirst();

        return maybeZap.isPresent();
    }

}