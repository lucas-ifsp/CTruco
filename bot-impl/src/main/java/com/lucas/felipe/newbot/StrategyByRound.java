package com.lucas.felipe.newbot;

import com.bueno.spi.model.CardToPlay;
import com.bueno.spi.model.GameIntel;
import com.bueno.spi.model.TrucoCard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public interface StrategyByRound {

    int getRaiseResponse (GameIntel intel);

    boolean getMaoDeOnzeResponse (GameIntel intel);

    boolean decideIfRaises (GameIntel intel);

    CardToPlay chooseCard (GameIntel intel);
}

