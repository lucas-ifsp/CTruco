package com.rennan.podecorrerpatinho;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;

import static com.rennan.podecorrerpatinho.PCPUtils.*;

public class PodeCorrerPatinho implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        int strength = handStrength(intel.getVira(), intel.getCards());
        int numManilhas = countManilhas(intel.getVira(), intel.getCards());

        if (hasCasalMaior(intel.getVira(), intel.getCards()) || numManilhas >= 2 || strength > 20) {
            return true;
        }

        return strength > 18;
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        int strength = handStrength(vira, hand);
        int numManilhas = countManilhas(vira, hand);

        if (countManilhas(vira, hand) >= 2 || hasCasalMaior(vira, hand)) {
            return true;
        }

        if (strength > 25) {
            return true;
        }
        
        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        List<TrucoCard> hand = intel.getCards();
        TrucoCard vira = intel.getVira();
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();

        int roundNumber = roundResults.size() + 1;

        // Primeira rodada: Cauteloso, se perdeu, joga a carta mais forte
        if (roundNumber == 1) {
            if (roundResults.size() > 0 && roundResults.get(roundResults.size() - 1) == GameIntel.RoundResult.LOST) {
                return CardToPlay.of(getStrongest(vira, hand));
            } else {
                return CardToPlay.of(getWeakest(vira, hand));
            }
        }

        // Segunda rodada: Se perdeu, jogue mais forte. Se ganhou, jogue mais cauteloso.
        if (roundNumber == 2) {
            if (roundResults.get(roundResults.size() - 1) == GameIntel.RoundResult.LOST) {
                return CardToPlay.of(getStrongest(vira, hand));
            } else {
                return CardToPlay.of(getWeakest(vira, hand));
            }
        }

        // Terceira rodada: Se já estiver ganhando, maximize o uso de manilhas e cartas fortes.
        if (roundNumber == 3) {
            if (roundResults.get(roundResults.size() - 1) == GameIntel.RoundResult.LOST) {
                return CardToPlay.of(getStrongest(vira, hand));
            } else {
                return CardToPlay.of(getWeakest(vira, hand));
            }
        }

        // Caso o round número não tenha sido encontrado, retorna a carta mais forte por padrão
        return CardToPlay.of(getStrongest(vira, hand));
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        List<GameIntel.RoundResult> roundResults = intel.getRoundResults();
        int numManilhas = countManilhas(intel.getVira(), intel.getCards());
        int strength = handStrength(intel.getVira(), intel.getCards());
        int roundNumber = roundResults.size() + 1;

        if (roundNumber == 1) {
            if (numManilhas >= 2 || strength > 20) {
                return 1;
            }
        }

        if (roundNumber == 2) {
            if (numManilhas > 0 || strength > 15) {
                return 1;
            }
        }

        if (roundNumber == 3) {
            if (numManilhas >= 1 || strength > 25) {
                return 1;
            }
        }

        return 0;
    }
}
