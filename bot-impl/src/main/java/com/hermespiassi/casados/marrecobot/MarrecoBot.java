package com.hermespiassi.casados.marrecobot;

import com.bueno.spi.model.CardSuit;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

import static com.bueno.spi.model.CardSuit.SPADES;

public class MarrecoBot implements BotServiceProvider {
    @Override
    public int getRaiseResponse(GameIntel intel) {
        System.out.println(intel.toString());
        return 0;
    }

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        List<TrucoCard> manilhas = cards.stream().filter(_card -> _card.isManilha(vira)).toList();
        if (!manilhas.isEmpty()) {
            Optional<TrucoCard> picaFumo = manilhas.stream().filter(_card -> _card.isOuros(vira)).findFirst();

            if (opponentCard.isPresent()) {
                if (picaFumo.isPresent() && !opponentCard.get().isManilha(vira)) return CardToPlay.of(picaFumo.get());
            } else {
                if (picaFumo.isPresent()) return CardToPlay.of(picaFumo.get());
            }
        }

        return CardToPlay.of(cards.get(0));
    }
}
