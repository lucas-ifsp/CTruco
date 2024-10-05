package com.manhani.stefane.reimubot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.Comparator;
import java.util.List;

public class ReimuBot implements BotServiceProvider {
    public static final int REFUSE = -1;
    public static final int ACCEPT = 0;
    public static final int RERAISE = 1;
    
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
        if(isFirstToPlayRound(intel))
            return CardToPlay.of(FirstToPlayStrategy(intel));
        return CardToPlay.of(LastToPlayStrategy(intel));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        return 0;
    }
    
    private int getHandValue(GameIntel intel) {
        return intel.getCards().stream().mapToInt(c -> c.relativeValue(intel.getVira())).sum();
    }
    
    //should only be called after checking if you're not first
    private boolean canDefeatOpponentCard(GameIntel intel) {
        return intel.getCards().stream()
                .anyMatch(c -> c.compareValueTo(intel.getOpponentCard().get(), intel.getVira()) > 0);
    }
    
    private boolean isFirstToPlayRound(GameIntel intel){
        return intel.getOpponentCard().isEmpty();
    }
    
    private TrucoCard getWeakestCard(GameIntel intel){
        return getWeakestCard(intel.getCards(), intel.getVira());
    }
    
    private TrucoCard getWeakestCard(List<TrucoCard> cards, TrucoCard vira){
        return cards.stream().min(Comparator.comparingInt(c->c.relativeValue(vira))).get();
    }

    //should only be called after checking if you're not first
    //should only be called after checking you can win
    private TrucoCard getWeakestCardThatWins(GameIntel intel){
        var vira = intel.getVira();
        var cards = intel.getCards().stream()
                .filter(c -> c.relativeValue(vira) > intel.getOpponentCard().get().relativeValue(vira))
                .toList();
        return getWeakestCard(cards, vira);
    }
    
    private TrucoCard FirstToPlayStrategy(GameIntel intel){
        return getWeakestCard(intel);
    }
    
    private TrucoCard LastToPlayStrategy(GameIntel intel){
        if(canDefeatOpponentCard(intel))
            return getWeakestCardThatWins(intel);
        return getWeakestCard(intel);
    }

}
