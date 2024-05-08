package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SlayerBot implements BotServiceProvider {

    SlayerBotUtils utils = new SlayerBotUtils();

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel game) {
        TrucoCard vira = game.getVira();
        int botScore = game.getScore();
        int opponentScore = game.getOpponentScore();
        List<TrucoCard> cards = game.getCards();

        if (botScore == 11 || opponentScore == 11) {
            return false;
        }

        List<TrucoCard> manilhasInHand = cards.stream()
                .filter(card -> card.isManilha(vira))
                .toList();

        if (manilhasInHand.size() == 3) {
            return true;
        }

        boolean hasTiedInFirstRound = game.getRoundResults().contains(GameIntel.RoundResult.DREW);

        boolean hasManilha = cards.stream()
                .anyMatch(card -> card.isManilha(vira));

        // Se  rodada anterior resultou em empate e o bot tem uma manilha deve pedir truco
        if (hasTiedInFirstRound && hasManilha) {
            return true;
        }

        List<GameIntel.RoundResult> roundResults = game.getRoundResults();
        boolean inThirdRound = roundResults.size() == 2 && (
                (roundResults.get(0) == GameIntel.RoundResult.WON && roundResults.get(1) == GameIntel.RoundResult.LOST) ||
                        (roundResults.get(0) == GameIntel.RoundResult.LOST && roundResults.get(1) == GameIntel.RoundResult.WON)
        );

        // Verifica se a ultima carta do bot pode vencer a do oponente
        if (inThirdRound && cards.size() == 1 && game.getOpenCards().size() == 2) {
            TrucoCard opponentCard = game.getOpenCards().get(1);
            TrucoCard lastCard = cards.get(0);

            if (lastCard.compareValueTo(opponentCard, vira) > 0) {
                return true;
            }
        }

        if (game.getOpenCards().size() == 2) {
            TrucoCard opponentCard = game.getOpenCards().get(1);

            boolean hasZap = cards.stream()
                    .anyMatch(card -> card.isZap(vira));

            boolean hasWinningCard = cards.stream()
                    .anyMatch(card -> card.compareValueTo(opponentCard, vira) > 0);

            // Pede truco se tiver  zap e uma carta que vence
            return hasZap && hasWinningCard;
        }
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);

        List<TrucoCard> botManilhas = utils.getManilhas(cards, vira);


        // Se o bot não tem manilhas, joga a carta mais fraca
        if (botManilhas.isEmpty()) {
            TrucoCard weakestCard = utils.getWeakestCard(cards, vira);
            return CardToPlay.of(weakestCard);
        }

        boolean hasTied = intel.getRoundResults().contains(GameIntel.RoundResult.DREW);

        if (opponentCard != null && !hasTied) {
            TrucoCard tieCard = cards.stream()
                    .filter(card -> !card.isManilha(vira) && card.getRank() == opponentCard.getRank())
                    .findFirst()
                    .orElse(null);

            if (tieCard != null) {
                return CardToPlay.of(tieCard);
            }
        }


        TrucoCard genericCard = cards.stream()
                .min(Comparator.comparingInt(card -> card.compareValueTo(opponentCard, vira)))
                .orElse(utils.getWeakestCard(cards, vira));

        return CardToPlay.of(genericCard);
    }



    @Override
    public int getRaiseResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        Set<CardRank> strongRanks = Set.of(CardRank.TWO, CardRank.THREE);

        boolean hasZap = cards.stream().anyMatch(card -> card.isZap(vira));
        boolean hasCopas = cards.stream().anyMatch(card -> card.isCopas(vira));
        boolean hasStrongCards = cards.stream().anyMatch(card -> strongRanks.contains(card.getRank()));

        if (hasCopas && hasStrongCards) {
            return 0;
        } else if (hasCopas && hasZap) {
            return 0;
        } else if (hasZap && hasStrongCards) {
            return 0;
        } else {
            return -1;
        }
    }
}
