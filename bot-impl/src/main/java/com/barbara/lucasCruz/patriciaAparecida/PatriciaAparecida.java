package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class PatriciaAparecida implements BotServiceProvider {

    //decide se o bot joga uma "mão de onze".
    // Retornar false significa sair da mão.
    //Retornar true significa aceitar e jogar uma mão de três pontos;
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
            if(intel.getOpponentScore() != 11) throw new IllegalArgumentException("Hand of Eleven can't be called without 11 Points");

        return false;

    }

    //decide se o bot inicia uma solicitação de aumento de ponto.
    //Retornar false significa não fazer nada.
    //Retornar true significa solicitar um aumento de ponto;
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    //fornece o cartão a ser jogado ou descartado na rodada atual.
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    //responde a uma solicitação de aumento de ponto em uma mão de truco.
    //O valor de retorno deve ser um dos seguintes:
    //-1 (sair), 0 (aceitar), 1 (re-aumentar/chamar);
    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    public int getNumberOfCardsInHand(GameIntel intel) {
        return intel.getCards().size();
    }

    public int getNumberOfPlayedCards(GameIntel intel){
        return  intel.getOpenCards().size();
    }

    public int getNumberOfRemainderCards(GameIntel intel) {
        return (40 - getNumberOfCardsInHand(intel) - getNumberOfPlayedCards(intel));
    }

    public int getNumberOfBestCardsInMyHand(TrucoCard card, GameIntel intel){
        int count = 0;

        for(int i = 0; i < getNumberOfCardsInHand(intel); i++){
            if(card.compareValueTo(intel.getCards().get(i),intel.getVira())<0){
                count ++;
            }
        }

        return count;
    }

    public int getNumberOfBestCardsPlayed(TrucoCard card, GameIntel intel){
        int count =0;

        for(int i = 0; i < getNumberOfPlayedCards(intel) ; i++){
            if(card.compareValueTo(intel.getOpenCards().get(i),intel.getVira())<0){
                count++;
            }
        }

        return count;
    }

    public int getNumberOfBestCardsKnown(TrucoCard card, GameIntel intel){
        return getNumberOfBestCardsInMyHand(card, intel) + getNumberOfBestCardsPlayed(card, intel);
    }

    public int getNumberOfBestCardsUnknown(TrucoCard card, GameIntel intel) {

        if (card.isManilha(intel.getVira())) {
            return ((13 - card.relativeValue(intel.getVira())) - getNumberOfBestCardsKnown(card, intel));
        }

        return ((4*(10-card.relativeValue(intel.getVira())))) - getNumberOfBestCardsKnown(card, intel);
    }

    public boolean winTheLastRound (GameIntel intel){
        if (intel.getRoundResults().isEmpty()) {
            return false;
        }
        return intel.getRoundResults().get(intel.getRoundResults().size()-1)
                == GameIntel.RoundResult.WON ;
    }

    public int getNumberOfOpponentsCards(GameIntel intel){
        return  3- intel.getRoundResults().size() - (winTheLastRound(intel) ? 0 : 1 );
    }

    public double probabilityONEOpponentCardIsBetter (TrucoCard card, GameIntel intel){
        return (double) (getNumberOfRemainderCards(intel) - getNumberOfBestCardsUnknown(card, intel)) /getNumberOfRemainderCards(intel);
        //probabilidadeOponenteNaoTerUMACartaMaior = ((qntCartasNaoVistas-qntCartasMaioresDesconhecidas)/qntCartasNaoVistas);
    }

    public double probabilityOpponentCardIsBetter (TrucoCard card, GameIntel intel){
        return (1 - (Math.pow(probabilityONEOpponentCardIsBetter(card,intel), getNumberOfOpponentsCards(intel))));
        //robabilidadeOponenteTerCartaMaior =
        //                ( 1 - (Math.pow(probabilidadeOponenteNaoTerUMACartaMaior, cartasNaMaoDoAdversario))) * 100;
    }

}
