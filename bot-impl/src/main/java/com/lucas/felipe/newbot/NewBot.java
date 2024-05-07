package com.lucas.felipe.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class NewBot implements BotServiceProvider {
    private List<TrucoCard> roundCards;
    private List<TrucoCard> ordendedCards; // ascending order
    private TrucoCard vira;

    Comparator<TrucoCard> byValue = (card1, card2) ->
            Integer.compare(card1.relativeValue(vira), card2.relativeValue(vira));

    private void setCards(GameIntel intel){
        this.vira = intel.getVira();
        this.roundCards = intel.getCards();
        this.ordendedCards = sortCards(roundCards);
    }

    private List<TrucoCard> sortCards(List<TrucoCard> cards){
        List<TrucoCard> sortedCards = new ArrayList<>(cards);
        sortedCards.sort(byValue);

        return sortedCards;
    }

    private boolean isPowerfull(List<TrucoCard> ordenedCards){
        return ordenedCards.get(2).relativeValue(vira) >= 9 && ordenedCards.get(1).relativeValue(vira) >= 9;
    }
    /*
    4 1
    5 2
    6 3
    7 4
    q 5
    j 6
    k 7
    a 8
    2 9
    3 10
    manilha 11 12 13 14
    */



    public boolean getMaoDeOnzeResponse(GameIntel intel){
        setCards(intel);
        return false;
    }

    public boolean decideIfRaises(GameIntel intel){
        setCards(intel);
        if (intel.getRoundResults().isEmpty()) { // first round
            if (isPowerfull(ordendedCards)){
                if (ordendedCards.get(2).relativeValue(vira) > 10) return true;
            }
        }
        return false;
    }

    public CardToPlay chooseCard(GameIntel intel){
        setCards(intel);
        Optional<TrucoCard> opponentCard = Optional.empty();
        if (intel.getOpponentCard().isPresent()) opponentCard = intel.getOpponentCard();

        // Primeiro round
        if (intel.getRoundResults().isEmpty()){
            // Oponente já jogou a carta?
            if (opponentCard.isPresent()) {
                // Tenta ganhar com as cartas menores até a maior, se não conseguirmos, joga a mais fraca
                if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
                    return CardToPlay.of(ordendedCards.get(0));
                }
                else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
                    return CardToPlay.of(ordendedCards.get(1));
                }
                else if (ordendedCards.get(2).compareValueTo(opponentCard.get(), vira) > 0) {
                    return CardToPlay.of(ordendedCards.get(2));
                } else return CardToPlay.of(ordendedCards.get(0));
            }
            // Se o oponente não jogou a carta, jogamos nossa carta do meio
            return CardToPlay.of(ordendedCards.get(1));

        } else if (intel.getRoundResults().size() == 1) {  // segundo round
            // Ganhamos ?
            if (intel.getRoundResults().get(0) == GameIntel.RoundResult.WON){
                // Oponente jogou a carta ?
                if (opponentCard.isPresent()) {
                    // Tentamos ganhar com as cartas mais fracas
                    if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
                        return CardToPlay.of(ordendedCards.get(0));
                    } else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
                        return CardToPlay.of(ordendedCards.get(1));
                    } else return CardToPlay.discard(ordendedCards.get(0));
                }
                // se o oponente não jogou, jogamos nossa carta mais forte
                return CardToPlay.of(ordendedCards.get(1));
                // Caso primeiro round empatou, jogamos a mais forte
            } else if (intel.getRoundResults().get(0) == GameIntel.RoundResult.DREW){
                return CardToPlay.of(ordendedCards.get(1));
                // Caso perdemos o primeiro round
            } else {
                // Se conseguirmos, ganhamos com nossas cartas mais fracas, se não der, perdemos :(
                if (opponentCard.isPresent()){
                    if (ordendedCards.get(0).compareValueTo(opponentCard.get(), vira) > 0) {
                        return CardToPlay.of(ordendedCards.get(0));
                    }
                    else if (ordendedCards.get(1).compareValueTo(opponentCard.get(), vira) > 0) {
                        return CardToPlay.of(ordendedCards.get(1));
                    } else return CardToPlay.discard(ordendedCards.get(0));
                }
                // se o oponente não jogou, jogamos nossa carta mais forte
                return CardToPlay.of(ordendedCards.get(1));
            }

        }
        // Terceiro round -> joga o que sobrou
        else {
            return CardToPlay.of(ordendedCards.get(0));
        }
    }

    public int getRaiseResponse(GameIntel intel){
        setCards(intel);
        return 1;
    }

}
