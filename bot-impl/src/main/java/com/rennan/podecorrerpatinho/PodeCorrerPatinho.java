package com.rennan.podecorrerpatinho;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Random;

public class PodeCorrerPatinho implements BotServiceProvider {
    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        if (intel.getOpponentScore() < 11) {
            if (PCPUtils.hasCasalMaior(intel.getVira(), intel.getCards())) return true;
            if (PCPUtils.hasCasalPreto(intel.getVira(), intel.getCards())) return true;
            if (PCPUtils.hasCasalMenor(intel.getVira(), intel.getCards())) return true;
            if (PCPUtils.hasCasalVermelho(intel.getVira(), intel.getCards())) return true;
            if (PCPUtils.hasZapOuros(intel.getVira(), intel.getCards())) return true;
            if (PCPUtils.hasCopasEspadilha(intel.getVira(), intel.getCards())) return true;
            if (PCPUtils.hasManilha(intel.getVira(), intel.getCards()) && PCPUtils.hasZap(intel.getVira(), intel.getCards())) return true;
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        if (intel.getScore() == 11 || intel.getOpponentScore() == 11) return false;

        int roundNumber = intel.getRoundResults().size() + 1;
        List<GameIntel.RoundResult> roundsResults = intel.getRoundResults();

        if (roundNumber == 1){
            return false;
        }else if (roundNumber == 2){
            if (roundsResults.get(0).equals(GameIntel.RoundResult.WON)){
                if (PCPUtils.hasZap(intel.getVira(), intel.getCards())) return false;

                if (PCPUtils.zapCopasAndEspadaAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasOuros(intel.getVira(),intel.getCards())) return true;

                if (PCPUtils.zapAndCopasAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasEspada(intel.getVira(), intel.getCards())) return true;

                if (PCPUtils.zapAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasCopas(intel.getVira(), intel.getCards())) return true;
            } else if (roundsResults.get(0).equals(GameIntel.RoundResult.DREW)){
                if (PCPUtils.hasZap(intel.getVira(), intel.getCards())) return true;
                if (PCPUtils.hasCopas(intel.getVira(), intel.getCards()) && intel.getHandPoints() < 3) return true;

            }
        } else if (roundNumber == 3) {
            if (intel.getOpponentCard().isPresent()){
                TrucoCard enemyCard = intel.getOpponentCard().get();
                if (PCPUtils.getStrongest(intel.getVira(), intel.getCards()).relativeValue(intel.getVira()) >
                        enemyCard.relativeValue(intel.getVira())) return true;
            }
            if (PCPUtils.hasZap(intel.getVira(), intel.getCards())) return true;
            if (PCPUtils.hasCopas(intel.getVira(), intel.getCards()) && intel.getHandPoints() < 3) return true;

            // Roleta russa :P
            if (new Random().nextInt(500) == 77 && intel.getHandPoints() < 3) {
                return true;
            }
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        int roundNumber = intel.getRoundResults().size() + 1;
        List<GameIntel.RoundResult> roundsResults = intel.getRoundResults();
        List<TrucoCard> myHand = intel.getCards();

        // Primeira é caminhão de boi >:(
        if (roundNumber == 1){
            if (intel.getOpponentCard().isPresent()){
                TrucoCard enemyCard = intel.getOpponentCard().get();
                if (PCPUtils.getStrongest(intel.getVira(), intel.getCards()).relativeValue(intel.getVira()) >
                        enemyCard.relativeValue(intel.getVira())){
                    for (TrucoCard myCard : myHand) {
                        if (myCard.relativeValue(intel.getVira()) > enemyCard.relativeValue(intel.getVira())) {
                            return CardToPlay.of(myCard);
                        }
                    }

                    return CardToPlay.of(PCPUtils.getWeakest(intel.getVira(), myHand));
                }
            }
            return CardToPlay.of(PCPUtils.getStrongest(intel.getVira(), myHand));
        } else if (roundNumber == 2) {
            if (roundsResults.get(0).equals(GameIntel.RoundResult.WON)){
                if (PCPUtils.hasZap(intel.getVira(), intel.getCards())){
                    return CardToPlay.of(PCPUtils.getWeakest(intel.getVira(), intel.getCards()));
                }
                if (PCPUtils.zapCopasAndEspadaAlreadyPlayed(intel.getVira(), intel.getOpenCards()) && PCPUtils.hasOuros(intel.getVira(),intel.getCards())){
                    return CardToPlay.of(PCPUtils.getWeakest(intel.getVira(), intel.getCards()));
                }
                if (PCPUtils.zapAndCopasAlreadyPlayed(intel.getVira(), intel.getOpenCards()) && PCPUtils.hasEspada(intel.getVira(), intel.getCards())){
                    return CardToPlay.of(PCPUtils.getWeakest(intel.getVira(), intel.getCards()));
                }
                if (PCPUtils.zapAlreadyPlayed(intel.getVira(), intel.getOpenCards()) && PCPUtils.hasCopas(intel.getVira(), intel.getCards())){
                    return CardToPlay.of(PCPUtils.getWeakest(intel.getVira(), intel.getCards()));
                }
            }
            if (roundsResults.get(0).equals(GameIntel.RoundResult.DREW) || roundsResults.get(0).equals(GameIntel.RoundResult.LOST)){
                return CardToPlay.of(PCPUtils.getStrongest(intel.getVira(), intel.getCards()));
            }
        }
        return CardToPlay.of(PCPUtils.getStrongest(intel.getVira(), intel.getCards()));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int roundNumber = intel.getRoundResults().size() + 1;
        List<GameIntel.RoundResult> roundsResults = intel.getRoundResults();

        if (roundNumber == 1) {
            if (PCPUtils.hasCasalMaior(intel.getVira(), intel.getCards())) return 1;
            if (PCPUtils.hasCasalPreto(intel.getVira(), intel.getCards())) return 1;
        } else if (roundNumber == 2) {
            if (roundsResults.get(0).equals(GameIntel.RoundResult.WON) ||
                    roundsResults.get(0).equals(GameIntel.RoundResult.DREW)) {
                if (PCPUtils.hasZap(intel.getVira(), intel.getCards())) return 1;
                if (PCPUtils.zapCopasAndEspadaAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasOuros(intel.getVira(), intel.getCards())) return 1;
                if (PCPUtils.zapAndCopasAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasEspada(intel.getVira(), intel.getCards())) return 1;
                if (PCPUtils.zapAlreadyPlayed(intel.getVira(), intel.getOpenCards())
                        && PCPUtils.hasCopas(intel.getVira(), intel.getCards())) return 1;
                if (PCPUtils.hasManilha(intel.getVira(), intel.getCards())) return 0;
                if (PCPUtils.hasThree(intel.getVira(), intel.getCards()) && new Random().nextInt(4) == 1) return 0;
            }

        } else if (roundNumber == 3) {
            if (intel.getOpponentCard().isPresent()){
                TrucoCard enemyCard = intel.getOpponentCard().get();
                if (PCPUtils.getStrongest(intel.getVira(), intel.getCards()).relativeValue(intel.getVira()) >
                        enemyCard.relativeValue(intel.getVira())) return 1;
            }
            if (PCPUtils.hasZap(intel.getVira(), intel.getCards())) return 1;
            if (PCPUtils.hasCopas(intel.getVira(), intel.getCards()) && intel.getHandPoints() < 3) return 0;
        }
        return -1;
    }
}
