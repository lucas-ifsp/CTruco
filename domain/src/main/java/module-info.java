module domain {
    requires java.logging;
    requires bot.spi;
    requires bot.impl;
    requires spring.context;
    requires lombok;

    exports com.bueno.domain.entities.game to application.persistence;
    exports com.bueno.domain.entities.deck to application.persistence;
    exports com.bueno.domain.entities.player to application.persistence;

    exports com.bueno.domain.usecases.game;
    exports com.bueno.domain.usecases.user;
    exports com.bueno.domain.usecases.hand;
    exports com.bueno.domain.usecases.bot;
    exports com.bueno.domain.usecases.intel;
    exports com.bueno.domain.usecases.utils.exceptions;
    exports com.bueno.domain.usecases.utils.dtos;
    exports com.bueno.domain.usecases.game.model;
    exports com.bueno.domain.usecases.intel.model;
    exports com.bueno.domain.usecases.user.model;
}