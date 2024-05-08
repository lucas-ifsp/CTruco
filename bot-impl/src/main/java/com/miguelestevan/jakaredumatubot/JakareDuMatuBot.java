package com.miguelestevan.jakaredumatubot;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.*;

public class JakareDuMatuBot implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if(getManilhas(intel.getCards(), intel.getVira()).containsAll(List.of(CardSuit.CLUBS, CardSuit.HEARTS))) return true;

        else if (getManilhas(intel.getCards(), intel.getVira()).size() >= 2) return true;

        else if ((getManilhas(intel.getCards(), intel.getVira()).size() == 1) && hasCardHigherThan(intel, TrucoCard.of(CardRank.KING, CardSuit.SPADES))) return true;

        else if(countCardsHigherThan(intel, CardRank.KING) >= 2) return true;

        return false;

    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {

        if(intel.getHandPoints() == 12){
            return false;
        }

        switch (intel.getRoundResults().size()){
            case 0 -> {
                // First Hand
                if(getManilhas(intel.getCards(), intel.getVira()).containsAll(List.of(CardSuit.CLUBS, CardSuit.HEARTS))){
                    // Hand contains zap and copas
                    return true;
                } else if (getManilhas(intel.getCards(), intel.getVira()).size() == 2 && intel.getScore()-intel.getOpponentScore()>=3) {
                    return true;
                }
            }
            case 1 -> {
                // Second Hand
                System.out.println("SECOND HAND");
            }
            case 2 -> {
                // First Hand
                System.out.println("THIRD HAND");
            }

        }


        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {

        switch (intel.getRoundResults().size()) {
            case 0 -> {
                // First Hand
                List<TrucoCard> list = sortedListCards(intel, intel.getVira());
                if (intel.getOpponentCard().isPresent()){
                    if (hasCardHigherThan(intel, intel.getOpponentCard().get())){
                        if (list.get(2).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0){
                            return CardToPlay.of(list.get(2));
                        } else if (list.get(1).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0) {
                            return CardToPlay.of(list.get(1));
                        }else{
                            return CardToPlay.of(list.get(0));
                        }
                    }
                    if (list.get(0).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) == 0){
                        return CardToPlay.of(list.get(0));
                    }
                    else if (list.get(1).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) == 0) {
                        return CardToPlay.of(list.get(1));
                    }
                    else  return CardToPlay.of(list.get(2));

                }
                return CardToPlay.of(list.get(0));
            }
            case 1->{
                // Second Hand
                // Responder a carta do oponente
                if(intel.getOpponentCard().isPresent()){
                    // Se tiver carta que mata
                    if (hasCardHigherThan(intel, intel.getOpponentCard().get())){
                        List<TrucoCard> list = sortedListCards(intel, intel.getVira());
                        if(list.get(1).compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0){
                            return CardToPlay.of(list.get(1));
                        }
                        return CardToPlay.of(list.get(0));
                    }
                    // Se não tiver carta -> retornar a carta mais fraca
                    return CardToPlay.of(sortedListCards(intel, intel.getVira()).get(1));

                }

                List<TrucoCard> sorted = sortedListCards(intel, intel.getVira());
                if(intel.getHandPoints() > 1){
                    // Está trucado
                    if(getManilhas(intel.getCards(), intel.getVira()).size() > 0){
                        return CardToPlay.of(sorted.get(0)); // Vai retornar a manilha
                    }
                    return CardToPlay.of(sorted.get(1));
                }

                // Não está trucado
                if(getManilhas(intel.getCards(), intel.getVira()).size() > 0){
                    return CardToPlay.of(sorted.get(1)); // Retorna a mais fraca
                }

                return CardToPlay.of(sorted.get(0));
            }
        }
        return null;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }

    @Override
    public String getName() {
        return "JakaréDuMatuBóty";
    }
    // The correct way to say is JakaréDuMatuBóty


    // This function returns a list of TrucoCards that are manilas
    private List<CardSuit> getManilhas(List<TrucoCard> botCards, TrucoCard vira) {
        return botCards.stream().filter(trucoCard -> trucoCard.isManilha(vira)).map(TrucoCard::getSuit).toList();
    }

    public boolean hasCardHigherThan(GameIntel intel, TrucoCard trucoCard) {
        return intel.getCards().stream().anyMatch(card -> card.compareValueTo(trucoCard, intel.getVira()) > 0);
    }

    public int countCardsHigherThan(GameIntel intel, CardRank cardRank) {
        return (int) intel.getCards().stream()
                .filter(card -> card.getRank().value() > cardRank.value())
                .count();
    }

    public List<TrucoCard> sortedListCards(GameIntel intel, TrucoCard vira){
//        intel.getCards().sort((o1, o2) -> o1.compareValueTo(o2, vira));
        List<TrucoCard> cards = new ArrayList<>(intel.getCards());
        cards.sort(new Comparator<TrucoCard>() {
            @Override
            public int compare(TrucoCard o1, TrucoCard o2) {
                return o1.compareValueTo(o2, vira);
            }
        });
        Collections.reverse(cards);
        return cards;

    }

}
