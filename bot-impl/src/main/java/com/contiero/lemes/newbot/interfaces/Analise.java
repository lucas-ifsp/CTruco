package com.contiero.lemes.newbot.interfaces;

public interface Analise {

    enum HandStatus {BAD,MEDIUM,GOOD,GOD};

    HandStatus myHand();
}
