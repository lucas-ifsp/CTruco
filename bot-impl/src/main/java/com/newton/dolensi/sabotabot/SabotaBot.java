package com.newton.dolensi.sabotabot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class SabotaBot implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        // nunca pedir truco na primeira rodada
        if (intel.getRoundResults().isEmpty()){
            return false;
        }


        // se ganhou a primeira rodada e tem manilha forte: truco
        if (roundResults.get(0) == GameIntel.RoundResult.WON) {
            if ( (!(getManilhasCard(intel).isEmpty())) && hasStrongManilha(intel)){
                return true;
            }
        }

        // se teve empate, o marreco jogou uma carta e temos uma mais forte: truco
        // se ganhou um dos dois primeiros rounds, o marreco jogou uma carta e temos uma mais forte: truco
        // se ganhou um dos dois primeiros rounds e temos uma manilha forte: truco
        if (
                (roundResults.contains(GameIntel.RoundResult.DREW)) ||
                (roundResults.contains(GameIntel.RoundResult.WON)))
        {
            if (canRise(intel)){
                return true;
            }
        }

        // se ganhamos a primeira e podemos empatar: truco
        if( (roundResults.size() > 1) && (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) ){
            if (!(intel.getOpponentCard().isEmpty())) {
                if (opponentHasTheSameCard(intel, intel.getOpponentCard().get())) {
                    return true;
                }
            }
        }

        return false;
    }



    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        var hand = intel.getCards();
        var opponentCard = intel.getOpponentCard();

        // se for o primeiro a jogar
        if (opponentCard.isEmpty()) {
            var strongCards = getStrongCardsNonManilha(intel, hand);
            for (TrucoCard card : hand) {
                // se tiver ouro e carta forte, já sai com ele
                if (card.isOuros(intel.getVira()) && !strongCards.isEmpty()) {
                    return CardToPlay.of(card);
                }
            }

            // sai com a mais forte que não seja manilha
            return CardToPlay.of(getGreatestCardNonManilha(intel, hand));
        } else if (opponentHasStrongCard(intel, opponentCard.get())) {
            // se o oponente jogar carta forte, joga a menor
            return CardToPlay.of(getWeakestCard(intel, hand));
        } else if (opponentHasTheSameCard(intel, opponentCard.get())){
            // se tiver uma igual e manilha, amarra pra jogar ela depois
            for (TrucoCard card : hand) {
                if (card.compareValueTo(opponentCard.get(), intel.getVira()) == 0
                && !getManilhasCard(intel).isEmpty())
                    return CardToPlay.of(card);
            }
        } else {
            return CardToPlay.of(getGreatestCardNonManilha(intel, hand));
        }
        return CardToPlay.of(hand.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    private TrucoCard getGreatestCardNonManilha(GameIntel intel, List<TrucoCard> hand) {
        var nonMalinhaCards = getNonMailhas(hand, intel.getVira());
        if (nonMalinhaCards.size() == 3) {
            if (nonMalinhaCards.get(0).compareValueTo(nonMalinhaCards.get(1), intel.getVira()) > 0) {
                if (nonMalinhaCards.get(0).compareValueTo(nonMalinhaCards.get(2), intel.getVira()) > 0)
                    return nonMalinhaCards.get(0);
                else
                    return nonMalinhaCards.get(2);
            } else if (nonMalinhaCards.get(1).compareValueTo(nonMalinhaCards.get(2), intel.getVira()) > 0)
                return nonMalinhaCards.get(1);
            else
                return nonMalinhaCards.get(2);
        } else if (nonMalinhaCards.size() == 2) {
            if (nonMalinhaCards.get(0).compareValueTo(nonMalinhaCards.get(1), intel.getVira()) > 0)
                return nonMalinhaCards.get(0);
            else
                return nonMalinhaCards.get(1);
        }
        return hand.get(0);
    }

    private List<TrucoCard> getNonMailhas(List<TrucoCard> hand, TrucoCard vira) {
        List<TrucoCard> nonManilhas = new ArrayList<>();
        for (TrucoCard card : hand) {
            if (!card.isManilha(vira))
                nonManilhas.add(card);
        }
        return nonManilhas;
    }

    private TrucoCard getWeakestCard(GameIntel intel, List<TrucoCard> hand) {
        if (hand.size() == 3) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) < 0) {
                if (hand.get(0).compareValueTo(hand.get(2), intel.getVira()) < 0)
                    return hand.get(0);
                else
                    return hand.get(2);
            } else if (hand.get(1).compareValueTo(hand.get(2), intel.getVira()) < 0)
                return hand.get(1);
            else
                return hand.get(2);
        } else if (hand.size() == 2) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) < 0)
                return hand.get(0);
            else
                return hand.get(1);
        }
        return hand.get(0);
    }

    private List<TrucoCard> getStrongCardsNonManilha(GameIntel intel, List<TrucoCard> hand) {
        int KING_VALUE = 7;
        List<TrucoCard> strongCards = new ArrayList<>();

        for (TrucoCard card : hand) {
            if (card.getRank().value() > KING_VALUE && !card.isManilha(intel.getVira())) {
                strongCards.add(card);
            }
        }
        return strongCards;
    }

    private boolean opponentHasStrongCard(GameIntel intel, TrucoCard opponentCard) {

        for (TrucoCard card : intel.getCards()) {
            if (card.compareValueTo(opponentCard, intel.getVira()) > 0){
                return false;
            }
        }
        return true;
    }

    private boolean opponentHasTheSameCard(GameIntel intel, TrucoCard opponentCard) {

        for (TrucoCard card : intel.getCards()) {
            if (card.compareValueTo(opponentCard, intel.getVira()) == 0){
                return true;
            }
        }
        return false;
    }

    private List<TrucoCard> getManilhasCard(GameIntel intel){

        List<TrucoCard> manilhas = new ArrayList<>();

        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) {
                manilhas.add(card);
            }
        }

        return manilhas;
    }

    private boolean hasStrongManilha(GameIntel intel) {

        TrucoCard vira = intel.getVira();
        List<TrucoCard> manilhas = getManilhasCard(intel);

        for (TrucoCard card : manilhas){
            if (card.isCopas(vira) || card.isZap(vira)){
                return true;
            }
        }

        return false;
    }

    private boolean canRise(GameIntel intel){

        if (!(intel.getOpponentCard().isEmpty())){

            if (!opponentHasStrongCard(intel, intel.getOpponentCard().get())){
                return true;
            }

        } else if (getManilhasCard(intel).size() > 0){
            if (hasStrongManilha(intel)){
                return true;
            }
        }

        return false;
    }
}
