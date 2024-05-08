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

        boolean opponentIsCopas = opponentCard != null && opponentCard.isCopas(vira);

        TrucoCard zap = botManilhas.stream()
                .filter(card -> card.isZap(vira))
                .findFirst()
                .orElse(null);

        // Se o oponente jogou uma manilha de copas e o bot tem o zap, joga o zap
        if (opponentIsCopas && zap != null && zap.compareValueTo(opponentCard, vira) > 0) {
            return CardToPlay.of(zap);
        }

        // Se o bot não tem manilhas, joga a carta mais fraca
        if (botManilhas.isEmpty()) {
            TrucoCard weakestCard = cards.stream()
                    .min(Comparator.comparingInt(card -> card.relativeValue(vira)))
                    .orElse(cards.get(0));
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

        if (hasTied && !botManilhas.isEmpty()) {
            TrucoCard strongestManilha = botManilhas.stream()
                    .max(Comparator.comparingInt(card -> card.relativeValue(vira)))
                    .orElse(botManilhas.get(0));
            return CardToPlay.of(strongestManilha);
        }

        TrucoCard genericCard = cards.stream()
                .min(Comparator.comparingInt(card -> card.compareValueTo(opponentCard, vira)))
                .orElse(cards.get(0));

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
