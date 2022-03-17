

module domain {
    requires java.logging;
    requires bot.spi;
    requires bot.impl;

    exports com.bueno.domain.entities.deck;
    exports com.bueno.domain.entities.player;
    //exports com.bueno.domain.entities.game to application.persistence;
    exports com.bueno.domain.entities.game;

    exports com.bueno.domain.usecases.utils;
    exports com.bueno.domain.usecases.game;
    exports com.bueno.domain.usecases.player;
    exports com.bueno.domain.usecases.hand.usecases;
    exports com.bueno.domain.usecases.hand.validators;
    exports com.bueno.domain.usecases.bot;
    exports com.bueno.domain.entities.hand;
    exports com.bueno.domain.entities.intel;
    exports com.bueno.domain.entities.hand.states;
}