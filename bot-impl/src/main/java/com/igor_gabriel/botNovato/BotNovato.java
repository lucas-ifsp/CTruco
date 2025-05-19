package com.igor_gabriel.botNovato;

import com.bueno.spi.model.*;
import com.bueno.spi.service.BotServiceProvider;

import java.util.List;
import java.util.Optional;

public class BotNovato implements BotServiceProvider {

    @Override
    public boolean getMaoDeOnzeResponse(GameIntel intel) {
        return isMaoForte(intel);
    }

    @Override
    public boolean decideIfRaises(GameIntel intel) {
        List<GameIntel.RoundResult> round = intel.getRoundResults();

        if (intel.getOpponentScore() == 11 || intel.getScore() == 11) {
            return false;  // não blefa se estiver perto de ganhar
        }

        if (round.isEmpty()) {
            return isMaoMuitoForte(intel);
        } else {
            GameIntel.RoundResult primeiroResultado = round.get(0);

            if (primeiroResultado == GameIntel.RoundResult.LOST || primeiroResultado == GameIntel.RoundResult.DREW) {
                return isMaoForte(intel) || isMaoMediaComManilha(intel);
            }

            if (primeiroResultado == GameIntel.RoundResult.WON) {
                return isMaoMediaComManilha(intel) || isMaoForte(intel);
            }
        }

        return false;
    }

    @Override
    public CardToPlay chooseCard(GameIntel intel) {
        Optional<TrucoCard> opponentCard = intel.getOpponentCard();
        TrucoCard vira = intel.getVira();
        List<TrucoCard> cards = intel.getCards();

        if (opponentCard.isPresent()) {
            TrucoCard oponente = opponentCard.get();
            TrucoCard deMenorQueGanha = getMenorQueGanha(cards, oponente, vira);
            if (deMenorQueGanha != null) {
                return CardToPlay.of(deMenorQueGanha);
            } else {
                return CardToPlay.of(getMenorCarta(cards, vira));
            }
        } else {
            if (isMaoMuitoForte(intel)) {
                return CardToPlay.of(getMenorCarta(cards, vira));
            }
            return CardToPlay.of(getMaiorCarta(cards, vira));
        }
    }

    @Override
    public int getRaiseResponse(GameIntel intel) {
        if (isMaoMuitoForte(intel)) return 1;
        if (isMaoForte(intel)) return 0;
        return -1;
    }

    @Override
    public String getName() {
        return "BotNovato";
    }


    private boolean isMaoMuitoForte(GameIntel intel) {
        return countManilhas(intel) >= 2 || (hasZap(intel) && countManilhas(intel) >= 1) || hasMaoGiga(intel);
    }

    private boolean isMaoForte(GameIntel intel) {
        return countManilhas(intel) >= 1 || hasCartasAltasComFiguras(intel);
    }

    private boolean isMaoMediaComManilha(GameIntel intel) {
        return countManilhas(intel) == 1 && hasCartasMedias(intel);
    }

    private boolean hasZap(GameIntel intel) {
        for (TrucoCard card : intel.getCards()) {
            if (card.relativeValue(intel.getVira()) == 13) return true;
        }
        return false;
    }

    private boolean hasMaoGiga(GameIntel intel) {
        return countManilhas(intel) >= 2;
    }

    private boolean hasCartasAltasComFiguras(GameIntel intel) {
        int figuras = 0, altas = 0;
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            int val = card.relativeValue(vira);
            if (val >= 7 && val <= 9) altas++;
            if (val >= 4 && val <= 6) figuras++;
        }

        return altas >= 1 && figuras >= 1;
    }

    private boolean hasCartasMedias(GameIntel intel) {
        TrucoCard vira = intel.getVira();

        for (TrucoCard card : intel.getCards()) {
            int val = card.relativeValue(vira);
            if (val < 7 || val > 9) return false;
        }

        return true;
    }

    private int countManilhas(GameIntel intel) {
        int count = 0;
        for (TrucoCard card : intel.getCards()) {
            if (card.isManilha(intel.getVira())) count++;
        }
        return count;
    }

    private TrucoCard getMenorCarta(List<TrucoCard> cards, TrucoCard vira) {
        TrucoCard menor = null;
        for (TrucoCard card : cards) {
            if (menor == null || card.relativeValue(vira) < menor.relativeValue(vira)) {
                menor = card;
            }
        }
        return menor;
    }

    private TrucoCard getMaiorCarta(List<TrucoCard> cards, TrucoCard vira) {
        TrucoCard maior = null;
        for (TrucoCard card : cards) {
            if (maior == null || card.relativeValue(vira) > maior.relativeValue(vira)) {
                maior = card;
            }
        }
        return maior;
    }

    private TrucoCard getMenorQueGanha(List<TrucoCard> cards, TrucoCard oponente, TrucoCard vira) {
        TrucoCard melhor = null;
        int valOponente = oponente.relativeValue(vira);

        for (TrucoCard card : cards) {
            int val = card.relativeValue(vira);
            if (val > valOponente) {
                if (melhor == null || val < melhor.relativeValue(vira)) {
                    melhor = card;
                }
            }
        }

        return melhor;
    }
}