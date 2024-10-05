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
        int strongCardsPlusManilhaCount = strongCards.size() + manilhas.size();


        if (myScore >= 9 || (myScore - opponentScore) > 6)  {
            if (goodCardsCount >= 2) {
                return true;
            }
        }

        if (opponentScore == 11) {
            return true;
        }

        if (opponentScore != 11) {
            if (round == 1) {
                if (strongCardsPlusManilhaCount >= 2) {
                    return true; // Pedir truco
                }
            }
            if (round == 2) {
                if (didIWinFirstRound(intel) && strongCardsPlusManilhaCount >= 1) {
                    return true; // Pedir truco
                }
                if (!didIWinFirstRound(intel) && strongCardsPlusManilhaCount >= 2) {
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
        int strongCardsCount = strongCards.size() + manilhas.size();
        int badCardsCount = badCards.size();
        TrucoCard theBestCard = getTheBestCard(intel);
        TrucoCard theWeakestCard = getWeakCard(myCards, intel);

        //---------------------------------------------------------
        if (round == 1) {
            if(isFirstToPlay(intel)){
                if (goodCardsCount == 3){
                    return CardToPlay.of(theBestCard);
                }
                if (goodCardsCount >= 1 || badCardsCount == 3) {
                    TrucoCard weakestCard = getWeakCard(myCards, intel);
                    return CardToPlay.of(weakestCard);
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
                        TrucoCard weakestCard = getWeakCard(myCards, intel);
                        return CardToPlay.of(weakestCard);
                    }
                }
            }
        }
        if (round == 2){
            if (isFirstToPlay(intel)) {
                if (didIWinFirstRound(intel)) {
                    System.out.println("retorna carta mais frac: " + theWeakestCard);
                    return CardToPlay.of(theWeakestCard);
                } else {
                    System.out.println("retorna best: " + theBestCard);
                    return CardToPlay.of(theBestCard);
                }
            } else {
                Optional<TrucoCard> opponentCardOpt = intel.getOpponentCard();
                if (opponentCardOpt.isPresent()) {
                    TrucoCard opponentCard = opponentCardOpt.get();
                    Optional<TrucoCard> winningCardOpt = findLowestWinningCard(opponentCard, myCards, intel.getVira());
                    System.out.println("win card:" + winningCardOpt);

                    if (winningCardOpt.isPresent()) {
                        return CardToPlay.of(winningCardOpt.get());
                    } else {
                        return CardToPlay.of(theWeakestCard);
                    }
                }
            }
        }
        if(round == 3){
            return CardToPlay.of(myCards.get(0));
        }

        return CardToPlay.of(myCards.get(0));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<TrucoCard> strongCards = getStrongCards(intel);
        List<TrucoCard> manilhas = getManilhas(intel);

        int strongCardsPlusManilhaCount = strongCards.size() + manilhas.size();
        int strongCardsCount = strongCards.size();
        int manilhasCount = manilhas.size();

        System.out.println("Strong Cards: " + strongCards);
        System.out.println("Manilhas: " + manilhas);
        System.out.println("Strong Cards Count: " + strongCardsCount);
        System.out.println("Get strong cards: " + strongCards);

        if (manilhasCount >= 2 && strongCardsCount >= 1){
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

    public TrucoCard getWeakCard(List<TrucoCard> myCards, GameIntel intel) {
        List<TrucoCard> noManilhas = myCards.stream()
                .filter(carta -> !carta.isManilha(intel.getVira()))
                .toList();

        List<TrucoCard> manilhas = getManilhas(intel);

        if (noManilhas.isEmpty()) {
            return manilhas.stream()
                    .min(Comparator.comparing(TrucoCard::getSuit))
                    .orElse(myCards.get(0));
        }

        return noManilhas.stream()
                .min(Comparator.comparing(carta -> carta.getRank().value()))
                .orElseThrow();
    }


    private Optional<TrucoCard> findLowestWinningCard(TrucoCard opponentCard, List<TrucoCard> cards, TrucoCard vira) {
        return cards.stream()
                .filter(card -> card.compareValueTo(opponentCard, vira) > 0)
                .min((card1, card2) -> card1.compareValueTo(card2, vira));
    }

    public List<TrucoCard> getStrongCards(GameIntel intel) {
        List<CardRank> strongRanks = List.of(CardRank.TWO, CardRank.THREE);
        return intel.getCards().stream()
                .filter(card -> strongRanks.contains(card.getRank()))
                .toList();
    }

    public List<TrucoCard> getGoodCards(GameIntel intel) {
        List<CardRank> goodRanks = List.of(CardRank.ACE, CardRank.KING, CardRank.JACK);
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
