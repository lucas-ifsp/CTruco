package com.barbara.lucasCruz.patriciaAparecida;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatriciaAparecida implements BotServiceProvider {


    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
            if(intel.getScore() != 11) throw new IllegalArgumentException("Hand of Eleven can't be called without 11 Points");

        return false;

    }

    //decide se o bot inicia uma solicitação de aumento de ponto.
    //Retornar false significa não fazer nada.
    //Retornar true significa solicitar um aumento de ponto;
    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if(intel.getHandPoints() > 1) throw new IllegalArgumentException("Cant call when already called");
        return false;
    }

    //fornece o cartão a ser jogado ou descartado na rodada atual.
    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        if(intel.getCards().isEmpty()) throw new IllegalStateException("Cannot choose a card without cards");


        if(intel.getOpponentCard().isPresent()){
              Optional<TrucoCard> weakestCardThatWins = getWeakestCardThatWins(intel.getCards(),intel);
              if(weakestCardThatWins.isPresent()) return CardToPlay.of(weakestCardThatWins.get());
              Optional<TrucoCard> cardThatDraws = getCardThatDraws(intel.getCards(),intel);
            if(intel.getRoundResults().isEmpty()) {
                if (cardThatDraws.isPresent()) return CardToPlay.of(cardThatDraws.get());
            }
              else return CardToPlay.discard(sortCards(intel.getCards(),intel).stream().findFirst().get());
        }
        return CardToPlay.of(intel.getCards().get(0));
        }

    private Optional<TrucoCard> getCardThatDraws(List<TrucoCard> cards, GameIntel intel) {
        for (TrucoCard card : cards)
            if(card.compareValueTo(intel.getOpponentCard().get(),intel.getVira()) == 0) return Optional.of(card);

        return Optional.empty();
    }

    private Optional<TrucoCard> getWeakestCardThatWins(List<TrucoCard> cards, GameIntel intel) {
         List<TrucoCard> tempcards = sortCards(cards, intel);
         tempcards = tempcards.stream().filter(trucoCard -> trucoCard.compareValueTo(intel.getOpponentCard().get(),intel.getVira()) > 0).toList();

        return tempcards.stream().findFirst();
    }

    private static List<TrucoCard> sortCards(List<TrucoCard> cards, GameIntel intel) {
        List<TrucoCard> tempcards = new ArrayList<>(cards);
        tempcards.sort((myCard,otherCard) -> myCard.compareValueTo(otherCard, intel.getVira()));
        return tempcards;
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

    public List<Double> listProbAllCards(GameIntel intel){
        List<Double> listProbAllCards = new ArrayList<>();
        for(int i=0; i<intel.getCards().size(); i++){
            listProbAllCards.add(probabilityOpponentCardIsBetter(intel.getCards().get(i),intel));
        }
        return listProbAllCards;
    }
}
