package com.hermespiassi.casados.marrecobot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Arrays;
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
                if (opponentCard.get().isOuros(vira)) {
                    if (manilhas.size() == 1) return CardToPlay.of(manilhas.get(0));
                    else if (manilhas.size() == 2) {
                        CardSuit suitFirstManilha = manilhas.get(0).getSuit();
                        CardSuit suitSecondManilha = manilhas.get(1).getSuit();
                        int lessSuit = suitFirstManilha.compareTo(suitSecondManilha);

                        if (lessSuit < 0) return CardToPlay.of(manilhas.get(0));
                        else return CardToPlay.of(manilhas.get(1));
                    } else if (manilhas.size() == 3) {
                        Optional<TrucoCard> zap = manilhas.stream().filter(_card -> _card.isZap(vira)).findFirst();
                        if (zap.isPresent()) return CardToPlay.of(zap.get());
                    }
                }
                if (opponentCard.get().isEspadilha(vira)) {
                    if (manilhas.size() == 1) {
                        CardSuit suitManilha = manilhas.get(0).getSuit();
                        if (suitManilha.compareTo(SPADES) > 0) return CardToPlay.of(manilhas.get(0));
                    } else if (manilhas.size() == 2) {
                        List<TrucoCard> notPicaFumo = manilhas.stream().filter(_manilha -> !_manilha.isOuros(vira)).toList();

                        if (notPicaFumo.size() == 1) return CardToPlay.of(notPicaFumo.get(0));

                        CardSuit suitFirstManilha = notPicaFumo.get(0).getSuit();
                        CardSuit suitSecondManilha = notPicaFumo.get(1).getSuit();
                        int lessSuit = suitFirstManilha.compareTo(suitSecondManilha);

                        if (lessSuit < 0) return CardToPlay.of(manilhas.get(0));
                        else return CardToPlay.of(manilhas.get(1));
                    }
                }
                if (opponentCard.get().isCopas(vira)) {
                    Optional<TrucoCard> zap = manilhas.stream().filter(card -> card.isZap(vira)).findFirst();
                    if (zap.isPresent()) return CardToPlay.of(zap.get());
                }
                if (picaFumo.isPresent() && !opponentCard.get().isManilha(vira)) return CardToPlay.of(picaFumo.get());
            } else {
                if (picaFumo.isPresent()) return CardToPlay.of(picaFumo.get());
            }
        } else {
            if (opponentCard.isPresent()) {
                if (opponentCard.get().isManilha(vira)){
                    TrucoCard firstCard = cards.get(0);
                    TrucoCard lowCard = firstCard;
                    for (TrucoCard card : cards) {
                        if (firstCard.getRank().compareTo(card.getRank()) > 0) lowCard = card;
                    }
                    return CardToPlay.of(lowCard);
                }
            }
        }

        return CardToPlay.of(cards.get(0));
    }
}
