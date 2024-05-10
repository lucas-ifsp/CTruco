package com.contiero.lemes.newbot.interfaces;

import com.bueno.spi.model.TrucoCard;

import java.util.List;

public interface Analise {

    enum HandStatus {BAD,MEDIUM,GOOD,GOD};

    HandStatus myHand();
}
