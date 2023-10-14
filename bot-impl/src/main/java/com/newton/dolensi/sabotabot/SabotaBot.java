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

        // nunca pedir truco na primeira rodada
        if (intel.getRoundResults().isEmpty()){
            return false;
        }

        // se ganhou a primeira rodada e tem manilha forte: truco
        if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON) {
            if ( (!(getManilhasCard(intel).isEmpty())) && hasStrongManilha(intel)){
                return true;
            }
        }

        // se teve empate, o marreco jogou uma carta e temos uma mais forte: truco
        // se ganhou um dos dois primeiros rounds, o marreco jogou uma carta e temos uma mais forte: truco
        if (
                (intel.getRoundResults().contains(GameIntel.RoundResult.DREW)) ||
                (intel.getRoundResults().contains(GameIntel.RoundResult.WON)))
        {
            if (canRise(intel)){
                return true;
            }
        }

        return false;
    }



    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        var hand = intel.getCards();
        var opponentCard = intel.getOpponentCard();
        CardToPlay cardToPlay = CardToPlay.of(hand.get(0));

        if (opponentCard.isEmpty()) {
            for (TrucoCard card : hand) {
                if (card.isOuros(intel.getVira())) {
                    return CardToPlay.of(card);
                }
            }
            cardToPlay = CardToPlay.of(getGreatestCard(intel, hand));
        } else if (opponentHasStrongCard(intel, opponentCard.get()))
            cardToPlay = CardToPlay.of(getWeakestCard(intel, hand));

        return cardToPlay;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    private TrucoCard getGreatestCard(GameIntel intel, List<TrucoCard> hand) {
        if (hand.size() == 3) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) > 0) {
                if (hand.get(0).compareValueTo(hand.get(2), intel.getVira()) > 0)
                    return hand.get(0);
                else
                    return hand.get(2);
            } else if (hand.get(1).compareValueTo(hand.get(2), intel.getVira()) > 0)
                return hand.get(1);
            else
                return hand.get(2);
        } else if (hand.size() == 2) {
            if (hand.get(0).compareValueTo(hand.get(1), intel.getVira()) > 0)
                return hand.get(0);
            else
                return hand.get(1);
        }
        return hand.get(0);
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

    private boolean opponentHasStrongCard(GameIntel intel, TrucoCard opponentCard) {

        for (TrucoCard card : intel.getCards()) {
            if (card.compareValueTo(opponentCard, intel.getVira()) > 0){
                return false;
            }
        }
        return true;
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

    private boolean canRise(GameIntel intel){

        if (!(intel.getOpponentCard().isEmpty())){

            if (!opponentHasStrongCard(intel, intel.getOpponentCard().get())){
                return true;
            }
        }

        return false;
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
}
