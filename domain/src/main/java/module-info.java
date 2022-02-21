module domain {
    requires java.logging;

    exports com.bueno.domain.entities.deck;
    exports com.bueno.domain.entities.player.util;
    exports com.bueno.domain.entities.game;

    exports com.bueno.domain.usecases.utils;
    exports com.bueno.domain.usecases.game;
    exports com.bueno.domain.usecases.player;
    exports com.bueno.domain.usecases.hand;
    exports com.bueno.domain.usecases.bot;
}