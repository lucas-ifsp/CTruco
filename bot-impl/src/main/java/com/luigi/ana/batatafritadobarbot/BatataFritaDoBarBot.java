package com.luigi.ana.batatafritadobarbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class BatataFritaDoBarBot implements BotServiceProvider {


    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(intel.getOpponentScore() == 11) return true;

        if(hasZap(intel) && hasCopas(intel)) return true;

        if(hasEspadilha(intel) && hasCopas(intel)) return true;

        if(getNumberOfManilhas(intel) > 1 && getAverageCardValue(intel) > 7 ) return true;

        if(intel.getOpponentScore() > 7) {
            return (getNumberOfManilhas(intel) > 0 && getAverageCardValue(intel) > 7);
        }
        if (intel.getOpponentScore() > 6) {
            return (getAverageCardValue(intel) >= 7);
        }

        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        int blefe = intel.getOpponentScore() - intel.getScore();

        if(intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;

        if(blefe > 7) return true;

        if(getNumberOfManilhas(intel) > 1) return true;

        if(intel.getOpponentScore() > 7) {
            return (getNumberOfManilhas(intel) > 0 && getAverageCardValue(intel) > 7);
        }
        if (intel.getOpponentScore() > 6) {
            return (getAverageCardValue(intel) >= 7);
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return switch (intel.getRoundResults().size() + 1) {
            case 1 -> chooseCardFirstRound(intel);
            case 2 -> chooseCardSecondRound(intel);
            case 3 -> chooseCardThirdRound(intel);
            default -> CardToPlay.of(intel.getCards().get(0));
        };
    }


    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (getNumberOfManilhas(intel) > 1) return 1;
        if (getNumberOfManilhas(intel) > 0 && getAverageCardValue(intel) >= 7) return 1;
        if (getAverageCardValue(intel) > 8) return 1;
        if (getAverageCardValue(intel) >= 6) return 0;
        return -1;
    }



    private CardToPlay chooseCardFirstRound(GameIntel intel){
        TrucoCard vira = intel.getVira();
        if(hasZap(intel) && hasCopas(intel)){
            return CardToPlay.of(getLowestCard(intel));
        }

        if(checkIfIsTheFirstToPlay(intel)){

            if(getNumberOfManilhas(intel) > 0){
                if (hasOuros(intel) || hasEspadilha(intel))
                    return CardToPlay.of(intel.getCards().stream()
                            .filter(trucoCard -> trucoCard.isOuros(vira) || trucoCard.isEspadilha(vira))
                            .findFirst()
                            .orElseGet(() -> getHighestNormalCard(intel)));

                if (hasZap(intel) || hasCopas(intel))
                    return CardToPlay.of( getHighestNormalCard(intel));

                else{
                    return CardToPlay.of(getHighestNormalCard(intel));
                }
            }

        }else{
            return getLowestToWin(intel)
                    .map(CardToPlay::of)
                    .orElseGet(() -> CardToPlay.of(getLowestCard(intel)));
        }

        return CardToPlay.of(getHighestCard(intel));

    }


    private CardToPlay chooseCardSecondRound(GameIntel intel){

        if(getlastRoundResult(intel).equals(GameIntel.RoundResult.WON)){

            if (getNumberOfManilhas(intel) == 1) {
                if(hasZap(intel) || hasCopas(intel)) return CardToPlay.of(getHighestNormalCard(intel));
                if(hasOuros(intel) || hasEspadilha(intel)) return CardToPlay.of(getHighestCard(intel));
            }
            if(getNumberOfManilhas(intel) > 1){
                return CardToPlay.discard(getLowestCard(intel));

            }else{
                if(getAverageCardValue(intel) > 8){
                    return CardToPlay.of(getLowestCard(intel));
                }else return CardToPlay.of(getHighestCard(intel));
            }

        }else{
            return CardToPlay.of(getHighestCard(intel));
        }

    }

    private CardToPlay chooseCardThirdRound(GameIntel intel){
        return CardToPlay.of(intel.getCards().get(0));
    }


    boolean checkIfIsTheFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }


    GameIntel.RoundResult getlastRoundResult(GameIntel intel){
        return intel.getRoundResults().get(0);
    }

    boolean isLastRoundWinner(GameIntel intel){
        return(GameIntel.RoundResult.WON).equals(getlastRoundResult(intel));

    }



    double getAverageCardValue(GameIntel intel) {

        return intel.getCards().stream()
                .mapToInt(card -> card.relativeValue(intel.getVira()))
                .average()
                .orElse(0.0);

    }



    int getNumberOfManilhas(GameIntel intel){
        int quantityOfManilhas = 0;
        for (TrucoCard card: intel.getCards())
            if(card.isManilha(intel.getVira()))
                quantityOfManilhas += 1;
        return quantityOfManilhas;
    }




    public TrucoCard getHighestCard(GameIntel intel) {
        TrucoCard highestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards())
            if (card.relativeValue(intel.getVira()) > highestCard.relativeValue(intel.getVira()))
                highestCard = card;
        return highestCard;
    }

    public TrucoCard getLowestCard(GameIntel intel) {
        TrucoCard lowestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards())
            if (lowestCard.relativeValue(intel.getVira()) > card.relativeValue(intel.getVira()))
                lowestCard = card;
        return lowestCard;
    }

    public TrucoCard getHighestNormalCard(GameIntel intel) {
        TrucoCard highestNormalCard = intel.getCards().get(0);
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards())
            if (!card.isManilha(vira) && card.relativeValue(intel.getVira()) > highestNormalCard.relativeValue(intel.getVira()))
                highestNormalCard = card;

        return highestNormalCard;
    }

    public Optional<TrucoCard> getLowestToWin(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        TrucoCard opponentCard = intel.getOpponentCard().orElse(null);

        if (opponentCard == null) {
            return Optional.empty();
        }

        TrucoCard lowestCard = intel.getCards().get(0);

        for (TrucoCard card : intel.getCards()){
            if (lowestCard.relativeValue(vira) > card.relativeValue(vira)
                    && card.relativeValue(vira) > opponentCard.relativeValue(vira))
                lowestCard = card;
        }
        return Optional.ofNullable(lowestCard);
    }



    boolean hasZap(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();

        TrucoCard vira = intel.getVira();

        boolean zap = false;

        for (TrucoCard card : myCards){

            if (card.isZap(vira)) zap = true;
        }

        return zap;
    }

    boolean hasCopas(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();

        TrucoCard vira = intel.getVira();

        boolean copas = false;

        for (TrucoCard card : myCards){

            if (card.isCopas(vira)) copas = true;
        }

        return copas;
    }

    boolean hasEspadilha(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();

        TrucoCard vira = intel.getVira();

        boolean espadilha = false;

        for (TrucoCard card : myCards){

            if (card.isEspadilha(vira)) espadilha = true;
        }

        return espadilha;
    }

    boolean hasOuros(GameIntel intel) {
        List<TrucoCard> myCards = intel.getCards();

        TrucoCard vira = intel.getVira();

        boolean ouros = false;

        for (TrucoCard card : myCards){

            if (card.isOuros(vira)) ouros = true;
        }

        return ouros;
    }

    public boolean isMaoDeFerro(GameIntel intel) {
        return isMaoDeOnze(intel) && intel.getOpponentScore() == 11;
    }


    public boolean isMaoDeOnze(GameIntel intel){
        return intel.getScore() == 11;
    }


}