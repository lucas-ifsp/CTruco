package com.otavio.lopes.teitasbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

public class TeitasBot implements BotServiceProvider {
    private List<TrucoCard> myCards;
    private TrucoCard vira;
    private TrucoCard bestCard;
    private TrucoCard secondBestCard;
    private TrucoCard worstCard;

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        Boolean hasNuts =  TeitasBotFunctions.hasNutsHand(cards,vira);
        Boolean hasStrong = TeitasBotFunctions.hasStrongHand(cards,vira);


        return hasNuts || hasStrong;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        final int elevenHandPoints = 11;
        final int maxHandPoints = 12;

        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();


        boolean HasNutsHand = TeitasBotFunctions.hasNutsHand(cards,vira);
        boolean HasTrashHand = TeitasBotFunctions.hasTrashHand(cards,vira);
        boolean HasGoodHand = TeitasBotFunctions.hasGoodHand(cards,vira);
        boolean HasStrongHand = TeitasBotFunctions.hasStrongHand(cards,vira);

        if (intel.getScore() == elevenHandPoints ||
                intel.getOpponentScore() == elevenHandPoints ||
                intel.getHandPoints() == maxHandPoints) {
            return false;
        }

        return HasNutsHand || HasStrongHand;

    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();
        Boolean handNuts = TeitasBotFunctions.hasNutsHand(cards,vira);
        Boolean handStrong = TeitasBotFunctions.hasStrongHand(cards,vira);
        Boolean handGood = TeitasBotFunctions.hasGoodHand(cards,vira);
        Boolean handTrash = TeitasBotFunctions.hasTrashHand(cards,vira);

        CardToPlay strongestCard = TeitasBotFunctions.getStrongestCard(cards,vira);
        CardToPlay secondBestCard = TeitasBotFunctions.getMiddleCardLevel(cards,vira);
        CardToPlay worstCard = TeitasBotFunctions.getWeakestCard(cards,vira);

        if(TeitasBotFunctions.firstToPlay(intel) & handNuts) {
            return secondBestCard;
        } else if (TeitasBotFunctions.firstToPlay(intel) & handStrong) {
            return worstCard;}
        else if(TeitasBotFunctions.firstToPlay(intel) & handGood) {
            return secondBestCard;
        } else if (TeitasBotFunctions.firstToPlay(intel) & handTrash) {
            return strongestCard;}

        TrucoCard strongCardTruco = TeitasBotFunctions.getStrongestCardTrucoCardType(cards,vira);
        TrucoCard middleCardTruco = TeitasBotFunctions.getMiddleCardTrucoCardType(cards,vira);
        TrucoCard weakestCardTruco = TeitasBotFunctions.getWeakestCardTrucoCardType(cards,vira);


        if (intel.getOpponentCard().isPresent()) {
            TrucoCard opponentCard = intel.getOpponentCard().get();

            if (strongCardTruco.compareValueTo(opponentCard, vira) > 0) {
                if (opponentCard.relativeValue(vira) < 8) {
                    return worstCard;
                }
                return secondBestCard;
            } else {
                return worstCard;
            }}

        return secondBestCard;
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        final int maoDeOnze = 11;
        final int maxHandPoints = 12;

        List<GameIntel.RoundResult> roundsAteAgora = intel.getRoundResults();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        Boolean myHandNuts = TeitasBotFunctions.hasNutsHand(cards,vira);
        Boolean myHandGood = TeitasBotFunctions.hasGoodHand(cards,vira);
        Boolean myHandStrong= TeitasBotFunctions.hasStrongHand(cards,vira);
        Boolean myHandsTrash= TeitasBotFunctions.hasTrashHand(cards,vira);

        if (intel.getScore() == maoDeOnze ||
                intel.getOpponentScore() == maoDeOnze ||
                intel.getHandPoints() == maxHandPoints) {
            return -1;
        }

        if (roundsAteAgora.isEmpty()) {
            if (myHandGood || myHandStrong) {

                return 0;
            } else if (myHandNuts) {
                return 0;
            }
        }


        if(!roundsAteAgora.isEmpty() && roundsAteAgora.get(0).equals(GameIntel.RoundResult.WON)){
            if (myHandNuts || myHandStrong) {
                return 1;
            }
            else if (myHandGood) {
                return 1;
            }
            else
                return -1;
        }

        if(!roundsAteAgora.isEmpty() && roundsAteAgora.get(0).equals(GameIntel.RoundResult.DREW)){
            //FODA SE
            return 1;
        }

        if(!roundsAteAgora.isEmpty() && roundsAteAgora.get(0).equals(GameIntel.RoundResult.LOST)) {
            if(myHandStrong | myHandNuts){
                return 1;
            }
            else if (myHandGood) {
                return 0;
            }
            else
                return -1;
        }
        return 0;
    }
}
