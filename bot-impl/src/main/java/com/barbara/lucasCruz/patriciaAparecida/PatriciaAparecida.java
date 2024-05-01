package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

public class PatriciaAparecida implements BotServiceProvider {

    //decide se o bot joga uma "mão de onze".
    // Retornar false significa sair da mão.
    //Retornar true significa aceitar e jogar uma mão de três pontos;
    @java.lang.Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) { return false; }

    //decide se o bot inicia uma solicitação de aumento de ponto.
    //Retornar false significa não fazer nada.
    //Retornar true significa solicitar um aumento de ponto;
    @java.lang.Override
    public boolean decideIfRaises(GameIntel intel) {
        return false;
    }

    //fornece o cartão a ser jogado ou descartado na rodada atual.
    @java.lang.Override
    public CardToPlay chooseCard(GameIntel intel) {
        return null;
    }

    //responde a uma solicitação de aumento de ponto em uma mão de truco.
    //O valor de retorno deve ser um dos seguintes:
    //-1 (sair), 0 (aceitar), 1 (re-aumentar/chamar);
    @java.lang.Override
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

}
