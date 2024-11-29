package com.lucassantos;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class TiaoDoCorote implements BotServiceProvider {
    private boolean alreadyRaisedThisHand = false;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return true; // sempre aceita mão de onze
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        // Se for mão de 11, não pede truco
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;

        // Se não for primeira rodada, não pede truco
        if (!intel.getRoundResults().isEmpty()) return false;

        // Se já pediu truco nesta mão, não pede novamente
        if (alreadyRaisedThisHand) return false;

        // Se o oponente jogou uma manilha e não tenho manilha, não peço truco
        if (intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();
            if (opponentCard.isManilha(intel.getVira())) {
                boolean hasManilha = intel.getCards().stream()
                        .anyMatch(card -> card.isManilha(intel.getVira()));
                if (!hasManilha) return false;
            }
        }

        alreadyRaisedThisHand = true;
        return true;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();
        List<GameIntel.RoundResult> results = intel.getRoundResults();

        // Se for a primeira rodada
        if (results.isEmpty()) {
            if (intel.getOpponentCard().isPresent()) {
                // Oponente jogou primeiro, tento matar com a menor carta possível
                TrucoCard opponentCard = intel.getOpponentCard().get();
                return CardToPlay.of(findSmallestWinningCard(myCards, opponentCard, intel.getVira()));
            } else {
                // Sou o primeiro a jogar, jogo a menor carta
                return CardToPlay.of(findLowestCard(myCards, intel.getVira()));
            }
        }

        // Se for a segunda rodada
        if (results.size() == 1) {
            if (results.get(0) == GameIntel.RoundResult.WON) {
                // Se ganhei a primeira, jogo a menor carta
                return CardToPlay.of(findLowestCard(myCards, intel.getVira()));
            } else {
                // Se perdi a primeira, tento matar com a menor carta possível
                if (intel.getOpponentCard().isPresent()) {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    return CardToPlay.of(findSmallestWinningCard(myCards, opponentCard, intel.getVira()));
                } else {
                    return CardToPlay.of(findLowestCard(myCards, intel.getVira()));
                }
            }
        }

        // Se for a terceira rodada, jogo a única carta que sobrou
        return CardToPlay.of(myCards.get(0));
    }

    private TrucoCard findSmallestWinningCard(List<TrucoCard> myCards, TrucoCard opponentCard, TrucoCard vira) {
        return myCards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min((card1, card2) -> card1.compareValueTo(card2, vira))
                .orElse(findLowestCard(myCards, vira)); // Se não tiver carta para matar, joga a mais baixa
    }

    private TrucoCard findLowestCard(List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .min((card1, card2) -> card1.compareValueTo(card2, vira))
                .orElse(cards.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0; // Por enquanto apenas aceita
    }

    @Override
    public String getName() {
        return "Tião do Coroté";
    }
}