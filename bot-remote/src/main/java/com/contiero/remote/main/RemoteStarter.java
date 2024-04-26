package com.contiero.remote.main;


import com.contiero.remote.utils.model.CardRank;
import com.contiero.remote.utils.model.CardSuit;
import com.contiero.remote.utils.model.GameIntel;
import com.contiero.remote.utils.model.TrucoCard;
import com.contiero.remote.utils.service.BotRemoteService;

import java.util.ArrayList;
import java.util.List;

public class RemoteStarter {
    public static void main(String[] args) {
        List<TrucoCard> cards = List.of(TrucoCard.of(CardRank.THREE, CardSuit.HEARTS));
        BotRemoteService bot = new BotRemoteService("http://localhost:8080/bot/maodeonze",
                "http://localhost:8080/bot/ifRaises",
                "http://localhost:8080/bot/chooseCard",
                "http://localhost:8080/bot/raiseResponse",
                "http://localhost:8080/bot/getName");
        GameIntel intel = new GameIntel(cards);
        System.out.println(bot.getMaoDeOnzeResponse(null));
        System.out.println(bot.decideIfRaises(null));
        System.out.println(bot.getRaiseResponse(null));
        System.out.println(bot.chooseCard(null));
        System.out.println(bot.getName());
    }
}