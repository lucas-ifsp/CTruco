package com.bianca.joaopedro.lgtbot;
import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class Lgtbot implements BotServiceProvider{
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        List<TrucoCard> strongCards = getStrongCards(intel);
        List<TrucoCard> goodCards = getGoodCards(intel);
        List<TrucoCard> manilhas = getManilhas(intel);

        int goodCardsCount = strongCards.size() + goodCards.size() + manilhas.size();
        int strongCardsCount = strongCards.size() + manilhas.size();

        if (intel.getOpponentScore() < 7 && goodCardsCount >= 2) {
            return true;
        }
        if (intel.getOpponentScore() < 11 && strongCardsCount >= 2) {
            return true;
        }
        if (intel.getOpponentScore() == 11){
            return true;
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int round = getRoundNumber(intel);
        int myScore = intel.getScore();
        int opponentScore = intel.getOpponentScore();
        List<TrucoCard> strongCards = getStrongCards(intel);
        List<TrucoCard> goodCards = getGoodCards(intel);
        List<TrucoCard> manilhas = getManilhas(intel);

        int goodCardsCount = strongCards.size() + goodCards.size() + manilhas.size();
        int strongCardsCount = strongCards.size() + manilhas.size();

        System.out.println("Strong Cards: " + strongCards);
        System.out.println("Ok Cards: " + goodCards);
        System.out.println("Manilhas: " + manilhas);
        System.out.println("Good Cards Count: " + goodCardsCount);
        System.out.println("Strong Cards Count: " + strongCardsCount);
        System.out.println("Dif: " + (myScore - opponentScore));
        System.out.println("Get strong cards: " + strongCards);
        System.out.println("Get good cards: " + goodCards);


        if (myScore >= 9 || (myScore - opponentScore) > 6)  {
            if (goodCards.size() >= 2) {
                return true;
            }
        }

        if (opponentScore == 11) {
            return true;
        }

        if (opponentScore != 11) {
            if (round == 1) {
                if (strongCardsCount >= 2) {
                    return true; // Pedir truco
                }
            }
            if (round == 2) {
                if (didIWinFirstRound(intel) && strongCardsCount >= 1) {
                    return true; // Pedir truco
                }
                if (!didIWinFirstRound(intel) && strongCardsCount >= 2) {
                    return true; // Pedir truco
                }
            }
            if (round == 3) {
                System.out.println("3 round");
                if (!manilhas.isEmpty()){
                    System.out.println("PEDE TRUCO");
                    return true; // Pedir truco
                }
            }
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int round = getRoundNumber(intel);
        List<TrucoCard> strongCards = getStrongCards(intel);
        List<TrucoCard> goodCards = getGoodCards(intel);
        List<TrucoCard> badCards = getBadCards(intel);
        List<TrucoCard> manilhas = getManilhas(intel);
        List<TrucoCard> myCards = intel.getCards();

        int goodCardsCount = strongCards.size() + goodCards.size() + manilhas.size();
        int badCardsCount = badCards.size();
        TrucoCard theBestCard = getTheBestCard(intel);
        TrucoCard theWeakestCard = getWeakCard(myCards);

        //---------------------------------------------------------
        if (round == 1) {
            if(isFirstToPlay(intel)){
                if (goodCardsCount == 2 && badCardsCount == 1) {
                    TrucoCard weakestCard = getWeakCard(badCards);
                    return CardToPlay.of(weakestCard);
                }
                if (goodCardsCount >= 1){
                    return CardToPlay.of(theBestCard);
                }
                if (badCardsCount == 3){
                    return CardToPlay.of(theBestCard);
                }
            }
            else{
                Optional<TrucoCard> opponentCardOpt = intel.getOpponentCard();
                if (opponentCardOpt.isPresent()) {
                    TrucoCard opponentCard = opponentCardOpt.get();
                    Optional<TrucoCard> winningCardOpt = findLowestWinningCard(opponentCard, myCards, intel.getVira());

                    if (winningCardOpt.isPresent()) {
                        return CardToPlay.of(winningCardOpt.get());
                    } else {
                        TrucoCard weakestCard = getWeakCard(badCards);
                        return CardToPlay.of(weakestCard);
                    }
                }
            }
        }
        if (round == 2){
            if (isFirstToPlay(intel)) {
                if (didIWinFirstRound(intel)) {
                    return CardToPlay.of(theWeakestCard);
                } else {
                    return CardToPlay.of(theBestCard);
                }
            } else {
                Optional<TrucoCard> opponentCardOpt = intel.getOpponentCard();
                if (opponentCardOpt.isPresent()) {
                    TrucoCard opponentCard = opponentCardOpt.get();
                    Optional<TrucoCard> winningCardOpt = findLowestWinningCard(opponentCard, myCards, intel.getVira());

                    if (winningCardOpt.isPresent()) {
                        return CardToPlay.of(winningCardOpt.get());
                    } else {
                        return CardToPlay.of(theWeakestCard);
                    }
                }
            }
        }
        if(round == 3){
            return CardToPlay.of(theBestCard);
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> strongCards = getStrongCards(intel);
        List<TrucoCard> manilhas = getManilhas(intel);

        int strongCardsPlusManilhaCount = strongCards.size() + manilhas.size();

        if (strongCardsPlusManilhaCount == 3){
            return 1;
        }
        else if (strongCardsPlusManilhaCount == 2){
            return 0;
        }
        else{
            return -1;
        }
    }

    public List<TrucoCard> getManilhas(GameIntel intel) {
        return intel.getCards().stream()
                .filter(carta -> carta.isManilha(intel.getVira()))
                .toList();
    }

    public int getRoundNumber(GameIntel intel) {
        return intel.getRoundResults().size() + 1;
    }

    public boolean didIWinFirstRound(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        if (!roundResults.isEmpty()) {
            if (roundResults.get(0) == GameIntel.RoundResult.WON) {
                return true;
            }
        }
        return false;
    }

    private boolean isFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    private TrucoCard getTheBestCard(GameIntel intel) {
        List<TrucoCard> cards = intel.getCards();
        if (cards.isEmpty()) {
            return null;
        }
        List<TrucoCard> manilhas = getManilhas(intel);
        if (!manilhas.isEmpty()) {
            return manilhas.stream()
                    .max(Comparator.comparing(TrucoCard::getRank)
                            .thenComparing(TrucoCard::getSuit))
                    .orElse(null);
        }

        return cards.stream()
                .max(Comparator.comparing(TrucoCard::getRank)
                        .thenComparing(TrucoCard::getSuit))
                .orElse(null);
    }

    private TrucoCard getWeakCard(List<TrucoCard> cards) {
        cards.sort(Comparator.comparing(TrucoCard::getRank));
        return cards.get(0);
    }

    private Optional<TrucoCard> findLowestWinningCard(TrucoCard opponentCard, List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min((card1, card2) -> card1.compareValueTo(card2, vira));
    }

    public List<TrucoCard> getStrongCards(GameIntel intel) {
        List<CardRank> strongRanks = List.of(CardRank.ACE, CardRank.TWO, CardRank.THREE);
        return intel.getCards().stream()
                .filter(card -> strongRanks.contains(card.getRank()))
                .toList();
    }

    public List<TrucoCard> getGoodCards(GameIntel intel) {
        List<CardRank> goodRanks = List.of(CardRank.KING, CardRank.JACK);
        return intel.getCards().stream()
                .filter(card -> goodRanks.contains(card.getRank()))
                .toList();
    }

    public List<TrucoCard> getBadCards(GameIntel intel) {
        List<CardRank> badRanks = List.of(CardRank.QUEEN, CardRank.SEVEN, CardRank.SIX, CardRank.FIVE, CardRank.FOUR);
        return intel.getCards().stream()
                .filter(card -> badRanks.contains(card.getRank()))
                .toList();
    }
}
