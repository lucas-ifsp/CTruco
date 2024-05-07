package com.petrilli.sandro.malasiabot;

import com.bueno.spi.model.CardRank;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MalasiaBot implements BotServiceProvider {

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (intel.getHandPoints() == 12)
            return -1;
        return 0;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        CardToPlay cardToPlay = null;

        if (intel.getOpponentCard().isPresent()) {
            cardToPlay = CardToPlay.of(DeMenorQuePodeGanhar(intel));
        }

        return cardToPlay;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    private boolean hasZapZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    private boolean TamoGiga(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()) || card.isCopas(intel.getVira()));
    }

    //retorna a menor carta da mão
    private TrucoCard DeMenor(GameIntel intel) {
        TrucoCard deMenor = null;
        for (TrucoCard card : intel.getCards()) {
            if (deMenor == null || card.relativeValue(intel.getVira()) < deMenor.relativeValue(intel.getVira())) {
                deMenor = card;
            }
        }
        return deMenor;
    }

    //retorna menor carta na mão que ganha da carta que o oponente jogou
    private TrucoCard DeMenorQuePodeGanhar(GameIntel intel) {
        TrucoCard DeMenorQuePodeGanhar = null;
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        if (opponentCard.isPresent()) {
            TrucoCard opponentCardValue = opponentCard.get();
            for (TrucoCard card : intel.getCards()) {
                int cardValue = card.relativeValue(vira);
                int opponentCardValueRelative = opponentCardValue.relativeValue(vira);
                if (cardValue > opponentCardValueRelative &&
                        (DeMenorQuePodeGanhar == null || cardValue < DeMenorQuePodeGanhar.relativeValue(vira))) {
                    DeMenorQuePodeGanhar = card;
                }
            }
        }
        return DeMenorQuePodeGanhar;
    }


}
