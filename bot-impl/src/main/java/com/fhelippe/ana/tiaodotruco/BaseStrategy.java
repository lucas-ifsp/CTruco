package com.fhelippe.ana.tiaodotruco;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseStrategy implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return handStrength(intel) > 27;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(wonFirstRound(intel)) {
            if(hasManilha(intel) || handStrength(intel) > 20) return true;

            if(handStrength(intel) > 25) return true;
        };

        if(!intel.getRoundResults().isEmpty()) {
            if(hasZap(intel) && hasCopas(intel)) return true;
        }

        return handStrength(intel) > 27 && hasManilha(intel);
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        CardToPlay weakestCard = CardToPlay.of(getWeakestCard(intel));
        CardToPlay strongestCard = CardToPlay.of(getStrongestCard(intel));

        if(intel.getRoundResults().isEmpty()){
            // #primeira mao
            //se caso houver copas e zap, joga a carta mais fraca
            if(hasCopas(intel) && hasZap(intel)) return weakestCard;
            //se caso nao houver manilha joga a mais forte
            if(!hasManilha(intel)) return weakestCard;
        }

        if(intel.getRoundResults().size() == 1) {
            // #segunda mao
            if (wonFirstRound(intel)) {
                if (hasZap(intel)) return CardToPlay.discard(getWeakestCard(intel));

                if (handStrength(intel) < 7) return strongestCard;
            }
            //nao fez a primeira joga a mais forte
            return strongestCard;
            //caso fez a primeira e tem zap, descarta a mais fraca encoberta
            //caso fez a primeira e a mão ta fraca joga a mais forte
        }

        return CardToPlay.of(getStrongestCard(intel));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if(hasZap(intel) && hasEspadilha(intel)) return 1;

        if(hasZap(intel) && hasEspadilha(intel)) return 1;

        if(intel.getRoundResults().isEmpty()) {
            if(hasManilha(intel) && hasBiggerThanTwo(intel).size() > 1) return 1;

            if(hasBiggerThanTwo(intel).size() > 1) return 0;
        }

        if(intel.getRoundResults().size() == 1) {
            if( wonFirstRound(intel) && (hasManilha(intel) || hasBiggerThanTwo(intel).size() > 1) ) return 1;
        }

        if(wonFirstRound(intel)) {
            if(intel.getOpponentCard().isPresent()){
                if( canKill(intel, intel.getCards().get(0)) ) return 1;

                else return -1;
            }

            if(handStrength(intel) > 9) return 0;
        }

        return -1;
    }

    ///////////////////////////////////////////////
    //Non Required methods
    ///////////////////////////////////////////////

    private CardToPlay playFirstRound(GameIntel intel){
        Optional<TrucoCard> midCard = getMidCard(intel);

        return midCard.map(CardToPlay::of)
                .orElse(CardToPlay.of(getStrongestCard(intel)));
    }

    private CardToPlay playSecondRound(GameIntel intel) {

        return getMidCard(intel).map(CardToPlay::of)
                .orElse(CardToPlay.of(getStrongestCard(intel)));
    }

    private TrucoCard chooseResponseCard(GameIntel intel) {
        TrucoCard responseCard = getWeakestCard(intel);

        return responseCard;
    }

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

    public Optional<TrucoCard> getStrongestCardWithoutManilha(GameIntel intel) {
        return intel.getCards().stream()
                .filter(e -> !e.isManilha(intel.getVira()))
                .max( (card1, card2) -> card1.compareValueTo(card2, intel.getVira()) );
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