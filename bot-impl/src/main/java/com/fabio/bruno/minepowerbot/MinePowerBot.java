package com.fabio.bruno.minepowerbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;


public class MinePowerBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(getCardAboveRank(intel, CardRank.ACE).size() == 3 && intel.getOpponentScore() <= 9)
            return true;
        if (listManilhas(intel).size()>=1)
            return true;
        return false;
    }

    private List<TrucoCard> getCardAboveRank(GameIntel intel, CardRank rank) {
        return intel.getCards().stream()
                .filter(card -> card.getRank().compareTo(rank) >= 0)
                .toList();
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        var manilhaFraca = getWeakerManilha(intel);
        var botScore = intel.getScore();
        var opponentScore = intel.getOpponentScore();
        var countManilhas = listManilhas(intel).size();
        if (botScore < 11) {
            if (botScore > opponentScore + 3)
                return true;
            if (countManilhas >= 1)
                return true;
            if (botScore != 0) {
                if (opponentScore <= 5)
                    return true;
                if (getCardAboveRank(intel, CardRank.TWO).size() >= 2)
                    return true;
                if (manilhaFraca != null)
                    return botScore == opponentScore || botScore > opponentScore;
            }
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int roundNumber = getRoundNumber(intel);
        TrucoCard vira = intel.getVira();

        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if (isTheFirstToPlay(intel)) {
            List<TrucoCard> qtdManilhas = listManilhas(intel);
            if (qtdManilhas.size() == 1) {
                TrucoCard manilha = qtdManilhas.get(0);
                return CardToPlay.of(manilha);
            } else {
                return CardToPlay.of(higherCard(intel));
            }
        }else if (!roundResults.isEmpty() && roundResults.get(0) == GameIntel.RoundResult.WON) { // joga uma carta baixa
            var lowCard = getLowerCard(intel);
            return CardToPlay.of(lowCard);
        } else {
            var opponentCard = intel.getOpponentCard().get();
            Optional<TrucoCard> lowestCardStrongerThanOpponentCard = intel.getCards().stream()
                    .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                    .min((card1, card2) -> card1.compareValueTo(card2, vira));
            if (lowestCardStrongerThanOpponentCard.isPresent())
                return CardToPlay.of(lowestCardStrongerThanOpponentCard.get());
            if (intel.getOpponentScore() == intel.getScore()){
                return CardToPlay.of(higherCard(intel));
            } else if (!roundResults.isEmpty() && roundResults.get(0) == GameIntel.RoundResult.WON) {
                return CardToPlay.of(getLowerCard(intel));
            }
        }
        return CardToPlay.of(intel.getCards().get(0));
    }

    public TrucoCard getLowerCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .min((card1, card2) -> card1.compareValueTo(card2, vira))
                .get();
    }

    public boolean hasZap(GameIntel intel) {
        return intel.getCards().stream().anyMatch(card -> card.isZap(intel.getVira()));
    }

    private List<TrucoCard> listManilhas(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        return intel.getCards().stream()
                .filter(card -> card.isManilha(vira))
                .toList();
    }

    private TrucoCard getWeakerManilha(GameIntel intel) {
        // Lista das manilhas na mão do jogador
        var manilhas = listManilhas(intel);

        // Carta do oponente
        var opponentCard = intel.getOpponentCard().orElse(null);

        // Se o oponente tiver jogado uma manilha
        if (opponentCard != null && opponentCard.isManilha(intel.getVira())) {
            // Inicializa a manilha mais fraca como null
            TrucoCard weakerManilha = null;

            // Para cada manilha na mão do jogador
            for (TrucoCard manilha : manilhas) {
                // Verifica se a manilha é mais forte que a do oponente
                if (manilha.compareValueTo(opponentCard, intel.getVira()) > 0) {
                    // Se for mais forte, atualiza a manilha mais forte
                    if (weakerManilha == null || manilha.relativeValue(intel.getVira()) < weakerManilha.relativeValue(intel.getVira())) {
                        weakerManilha = manilha;
                    }
                }
            }
            // Retorna a manilha mais forte, ou null se não houver manilhas mais fortes que a do oponente
            return weakerManilha;
        } else {
            // Se o oponente não tiver jogado uma manilha, retorna null
            return null;
        }
    }

    private boolean isTheFirstToPlay(GameIntel intel) {
        return intel.getOpponentCard().isEmpty();
    }

    private int getRoundNumber(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        var hasZap = hasZap(intel);
        var qtdManilhas = listManilhas(intel).size();
        if (hasZap && qtdManilhas >= 2) {
            return 1;
        } else if (hasZap || qtdManilhas >= 2) {
            return 0;
        }
        return -1;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }

    private TrucoCard higherCard(GameIntel intel) {
        var vira = intel.getVira();
        TrucoCard higherCard = null;
        for (TrucoCard card : intel.getCards()) {
            if (higherCard == null || card.relativeValue(vira) > higherCard.relativeValue(vira)) {
                higherCard = card;
            }
        }
        return higherCard;
    }
}
