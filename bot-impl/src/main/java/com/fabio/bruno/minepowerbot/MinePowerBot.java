package com.fabio.bruno.minepowerbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;


public class MinePowerBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    private List<TrucoCard> getCardAboveRank(GameIntel intel, CardRank rank){
        TrucoCard vira = intel.getVira();
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
        if(intel.getScore() > opponentScore + 3)
            return true;
        if(opponentScore <= 5)
            return true;
        if (countManilhas >=2)
            return true;
        if (getCardAboveRank(intel, CardRank.TWO).size() >= 2)
            return true;
        if (manilhaFraca != null)
            if (botScore == opponentScore || botScore > opponentScore) {
                return true;
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int roundNumber = getRoundNumber(intel);
        if (!isTheFirstToPlay(intel)) {
            switch(roundNumber) {
                case 1 -> {
                    TrucoCard opponentCard = intel.getOpponentCard().get();
                    TrucoCard vira = intel.getVira();
                    var lowestCardStrongerThanOpponentCard =  intel.getCards().stream()
                            .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                            .min( (card1, card2) -> card1.compareValueTo(card2, vira));
                    if (lowestCardStrongerThanOpponentCard.isPresent())
                        return CardToPlay.of(lowestCardStrongerThanOpponentCard.get());
                }
            }
        }
        return CardToPlay.of(intel.getCards().get(0));
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
        return 0;
    }

    @Override
    public String getName() {
        return BotServiceProvider.super.getName();
    }
}
