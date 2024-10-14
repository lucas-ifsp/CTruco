package com.brito.macena.boteco.interfaces;

import com.brito.macena.boteco.utils.Status;

public abstract class Analyzer {
    public abstract Status threeCardsHandler();
    public abstract Status twoCardsHandler();
    public abstract Status oneCardHandler();
}
