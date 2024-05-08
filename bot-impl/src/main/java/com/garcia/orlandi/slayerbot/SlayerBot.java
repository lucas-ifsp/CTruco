package com.garcia.orlandi.slayerbot;

import com.bueno.spi.model.*;
import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SlayerBot implements BotServiceProvider {

    SlayerBotUtils utils = new SlayerBotUtils();

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        TrucoCard vira = intel.getVira();

        List<TrucoCard> manilhas = utils.getManilhas(cards, vira);
        List<TrucoCard> threes = utils.getThreesAtHand(cards);

        if(manilhas.isEmpty()){
            return false;
        }else if(manilhas.size() == 2 || !threes.isEmpty()){
            return true;
        }

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
        List<TrucoCard> openCards = intel.getOpenCards();
        List<GameIntel.RoundResult> roundResult = intel.getRoundResults();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);

        if(roundResult.contains(GameIntel.RoundResult.DREW)){
            TrucoCard strongest = utils.getStrongestCard(cards, vira);
            return CardToPlay.of(strongest);
        }

        if(openCards.size() == 1) {
            //Play second strongest card if in first round or if lost first round
            if (roundResult.isEmpty()) {
                TrucoCard strongestCard = utils.getStrongestCard(cards, vira);
                TrucoCard secondStrongestCard = utils.getSecondStrongestCard(cards, strongestCard, vira);
                return CardToPlay.of(secondStrongestCard);
            } else  {
                for (TrucoCard card : cards) {
                    //Testing in order to play best manilha first, if found
                    if (card.isZap(vira) || card.isCopas(vira) || card.isEspadilha(vira) || card.isOuros(vira)) {
                        return CardToPlay.of(card);
                    }
                }
                //If has no manilha, returns the strongest card
                return CardToPlay.of(utils.getStrongestCard(cards, vira));
            }
        }else if(openCards.size() > 1 && opponentCard!=null){

            TrucoCard tieCard = cards.stream()
                    .filter(card -> !card.isManilha(vira) && card.compareValueTo(opponentCard, vira) == 0)
                    .findFirst()
                    .orElse(null);

            if (tieCard != null) {
                return CardToPlay.of(tieCard);
            }


            TrucoCard genericCard = cards.stream()
                    .min(Comparator.comparingInt(card -> card.compareValueTo(opponentCard, vira)))
                    .orElse(utils.getWeakestCard(cards, vira));

            return CardToPlay.of(genericCard);
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
