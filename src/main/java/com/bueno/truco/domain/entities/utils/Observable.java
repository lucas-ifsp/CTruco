package com.bueno.truco.domain.entities.utils;

public interface Observable {
    void registerObserver(Observer observer);
    void notifyObservers();
}
