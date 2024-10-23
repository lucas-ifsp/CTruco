package com.murilo.joao.jackbot;

import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

public class TrucoState implements RaiseResponseStatePattern {
    JackBot bot = new JackBot();

    @Override
    public int getRaiseResponse(GameIntel intel) {
        int roundNumber = bot.getRoundNumber(intel);

        // Verificar se o oponente tem um score maior que 9 e o bot tem um score menor que o oponente

        return switch (roundNumber) {
            case 1 ->
                // Lógica específica para o primeiro round
                    handleFirstRound(intel);
            case 2 ->
                // Lógica específica para o segundo round
                    handleSecondRound(intel);
            case 3 ->
                // Lógica específica para o terceiro round
                    handleThirdRound(intel);
            default -> throw new IllegalArgumentException("Número de round inválido: " + roundNumber);
        };
    }

    @Override
    public int handleFirstRound(GameIntel intel) {
        if (bot.countManilhas(intel) > 0 || bot.getCurrentHandValue(intel) > 22) {
            return 0; // Aceita
        } if (bot.countManilhas(intel) == 2 || bot.hasOneManilhaAndReallyGoodCard(intel)) {
            return 1; // Re-raise
        } if (bot.hasExtremeHand(intel) || bot.hasInvincibleHand(intel)) {
            // Se tiver uma mão super, super plus, extrema ou invencível, sobe o valor para seis
            return 1; // Re-raise
        } else {
            // Caso contrário, recuse, para garantir que funcione.
            return -1; // Fuga por padrão
        }
    }

    @Override
    public int handleSecondRound(GameIntel intel) {
        if (hasStrongSecondRoundHand(intel)) {
            return 1; // Re-raise
        } if (hasModerateSecondRoundHand(intel)) {
            return 0; // Aceita
        } else {
            return -1; // Quit
        }
    }

    @Override
    public boolean hasStrongSecondRoundHand(GameIntel intel) {
        return (bot.hasManilha(intel) && bot.wonFirstRound(intel)) ||
                (bot.wonFirstRound(intel) && bot.getCurrentHandValue(intel) >= 17) ||
                (bot.getCurrentHandValue(intel) >= 21 && bot.hasManilha(intel) && bot.lostFirstRound(intel)) ||
                (bot.getCurrentHandValue(intel) >= 21 && bot.drewFirstRound(intel)) ||
                (bot.hasZap(intel) && bot.drewFirstRound(intel));
    }

    @Override
    public boolean hasModerateSecondRoundHand(GameIntel intel) {
        return (bot.getCurrentHandValue(intel) >= 14 && bot.wonFirstRound(intel)) ||
                (bot.getCurrentHandValue(intel) < 21 && bot.hasZap(intel) && bot.lostFirstRound(intel)) ||
                (bot.getCurrentHandValue(intel) >= 17 && bot.drewFirstRound(intel));
    }


    @Override
    public int handleThirdRound(GameIntel intel) {
        if (intel.getCards().size() == 1) {
            if (intel.getCards().get(0).isZap(intel.getVira())) return 1;
        }
        TrucoCard cardPlayed = intel.getOpenCards().get(intel.getOpenCards().size() - 1);
        if (cardPlayed.isZap(intel.getVira())) return 1;
        if (bot.drewFirstRound(intel) || bot.drewSecondRound(intel) && bot.hasManilha(intel)) return 1;
        if (bot.wonFirstRound(intel) || bot.wonSecondRound(intel) && bot.hasManilha(intel)) return 1;
        if (bot.lostSecondRound(intel) || bot.lostFirstRound(intel) && bot.getCurrentHandValue(intel) >= 8 ) return 0;
        return -1;
    }

}
