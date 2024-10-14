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
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    int getNumberOfManilhas(GameIntel intel){
        return 0;
    }


    boolean checkIfIsTheFirstToPlay(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }

    public TrucoCard getHighestCard(GameIntel intel) {
        TrucoCard highestCard = intel.getCards().get(0);

        return highestCard;
    }

    public TrucoCard getLowestCard(GameIntel intel) {
        TrucoCard lowestCard = intel.getCards().get(0);

        return lowestCard;
    }

    public TrucoCard getHighestNormalCard(GameIntel intel) {
        TrucoCard lowestNormalCard = intel.getCards().get(0);

        return lowestNormalCard;
    }

    public Optional<TrucoCard> getLowestToWin(GameIntel intel) {
        TrucoCard lowestCard = intel.getCards().get(0);

        return Optional.ofNullable(lowestCard);
    }

    GameIntel.RoundResult getlastRoundResult(GameIntel intel){
        return intel.getRoundResults().get(0);
    }

    boolean isLastRoundWinner(GameIntel intel){
        return(GameIntel.RoundResult.WON).equals(getlastRoundResult(intel));

    }

    boolean hasZap(GameIntel intel) {
        return false;
    }

    boolean hasCopas(GameIntel intel) {
        return false;
    }

    boolean hasEspadilha(GameIntel intel) {
        return false;
    }

    boolean hasOuros(GameIntel intel) {
        return false;
    }

    public boolean isMaoDeFerro(GameIntel intel) {
        return false;
    }

    public boolean isMaoDeOnze(GameIntel intel){
        return false;
    }

}