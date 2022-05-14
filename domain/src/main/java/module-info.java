module domain {
    requires java.logging;
    requires bot.spi;
    requires bot.impl;
    requires spring.context;

    exports com.bueno.domain.entities.game to application.persistence;
    exports com.bueno.domain.entities.deck to application.persistence;
    exports com.bueno.domain.entities.player to application.persistence;

    exports com.bueno.domain.usecases.game;
    exports com.bueno.domain.usecases.game.dtos;
    exports com.bueno.domain.usecases.user;
    exports com.bueno.domain.usecases.user.dtos;
    exports com.bueno.domain.usecases.intel;
    exports com.bueno.domain.usecases.intel.dtos;
    exports com.bueno.domain.usecases.hand;
    exports com.bueno.domain.usecases.bot.providers;
    exports com.bueno.domain.usecases.utils.exceptions;
    exports com.bueno.domain.usecases.hand.dtos;
}