 package com.kayky.waleska.kwtruco;

import com.bueno.spi.service.BotServiceProvider;
import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class kwtruco implements BotServiceProvider {
    private static final List<CardRank> offCards = List.of(CardRank.ACE, CardRank.TWO, CardRank.THREE);
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (oponnentHasZap(intel)){
            return false;
        }
        if (intel.getOpponentScore() == 11){
            return true;
        }


        return hasManilhaAndHighRank(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if (intel.getOpponentScore() >= 9) {
            return false;
        }
        if (intel.getOpponentCard().isPresent()) {
            if (getMaxCardValue(intel) <= intel.getOpponentCard().get().relativeValue(intel.getVira())) {
                return false;
            }
        }

        if (intel.getOpponentScore() == 0 || intel.getScore() >= intel.getOpponentScore() + 3 || hasHigherThanAverageValue
                (intel) || hasManilhaAndHighRank(intel)) {
            return true;
        }
        return false;
    }

    private int getMaxCardValue(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        return cards.stream().mapToInt(card -> card.relativeValue(vira)).max().orElse(0);
    }

    private boolean hasHigherThanAverageValue(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        int handValue = cards.stream().mapToInt(card -> card.relativeValue(vira)).sum();

        return handValue >= 18;
    }

    private boolean hasManilhaAndHighRank(GameIntel intel) {
        List<TrucoCard> cartas = intel.getCards();
        TrucoCard vira = intel.getVira();

        boolean hasManilha = false;
        boolean hasCartaHigh = false;


        for (TrucoCard carta : cartas) {
            if (carta.isManilha(vira)) {
                hasManilha= true;
            }
            else if (carta.getRank().value() > 4) {
                hasCartaHigh = true;
            }
        }
        if (hasCartaHigh && hasManilha){
            return true;
        }
        return false;
    }



    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        // Verifica se há uma carta de ouros que seja manilha com base na vira
        for (TrucoCard card : intel.getCards()) {
            if (card.isOuros(intel.getVira())) {
                // Retorna a carta de ouros como a jogada a ser feita
                return CardToPlay.of(card);
            }
        }

        // Se ainda não houve resultado nas rodadas anteriores
        if (intel.getRoundResults().isEmpty()) {
            // Tenta pegar a menor carta de ataque disponível
            TrucoCard smallestAttackCard = findLowestAttackCard(intel);
            if (smallestAttackCard != null) {
                return CardToPlay.of(smallestAttackCard);
            }
        }

        // Escolhe a menor carta possível que possa vencer a carta do oponente
        TrucoCard smallestCardThatCanWin = selectSmallestCardThatCanWin(intel);

        // Se não houver carta capaz de vencer, escolhe a carta de menor valor da mão
        if (smallestCardThatCanWin == null) {
            smallestCardThatCanWin = findSmallestCardInHand(intel);
        }

        // Retorna a carta selecionada para ser jogada
        return CardToPlay.of(smallestCardThatCanWin);
    }

    private static TrucoCard findLowestAttackCard(GameIntel intel) {
        // Filtra as cartas de ataque na mão do jogador
        List<TrucoCard> attackCards = intel.getCards().stream()
                .filter(card -> offCards.contains(card.getRank()))
                .toList();

        // Se o jogador tiver pelo menos duas cartas de ataque, retorna a menor delas
        if (attackCards.size() >= 2) {
            return attackCards.stream()
                    .min(TrucoCard::relativeValue)
                    .orElse(null); // Usando orElse para maior clareza
        }

        // Se o jogador não tiver cartas de ataque, retorna null
        return null;
    }
    private TrucoCard selectSmallestCardThatCanWin(GameIntel intel) {
        // Obtém a carta do oponente, se houver.
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();

        // Se não houver carta do oponente, retorna null.
        if (opponentCard.isEmpty()) {
            return null;
        }

        // Obtém a carta vira.
        TrucoCard vira = intel.getVira();

        // Inicializa a menor carta que pode vencer.
        TrucoCard smallestCardThatCanWin = null;

        // Itera sobre todas as cartas na mão do jogador.
        for (TrucoCard card : intel.getCards()) {
            // Se a carta atual for maior que a carta do oponente, atualiza a menor carta que pode vencer.
            if (card.relativeValue(vira) > opponentCard.get().relativeValue(vira)) {
                // Se ainda não houver uma carta registrada ou se a nova carta for menor em relação à vira, atualiza.
                if (smallestCardThatCanWin == null || card.relativeValue(vira) < smallestCardThatCanWin.relativeValue(vira)) {
                    smallestCardThatCanWin = card;
                }
            }
        }

        // Retorna a menor carta que pode vencer.
        return smallestCardThatCanWin;
    }

    private TrucoCard findSmallestCardInHand(GameIntel intel) {
        // Inicializa a carta com o menor valor relativo em relação à carta vira.
        TrucoCard smallestCard = null;

        // Obtém a carta vira.
        TrucoCard vira = intel.getVira();

        // Itera sobre todas as cartas na mão do jogador.
        for (TrucoCard card : intel.getCards()) {
            // Se a carta atual tiver um valor relativo menor que a carta com o menor valor relativo, atualiza.
            if (smallestCard == null || card.relativeValue(vira) < smallestCard.relativeValue(vira)) {
                smallestCard = card;
            }
        }

        // Retorna a carta com o menor valor relativo em relação à carta vira.
        return smallestCard;
    }




    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    public boolean hasManilha(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isManilha(intel.getVira()));
    }

    private boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }
    private boolean oponnentHasZap(GameIntel intel) {
        return intel.getOpponentCard().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

}