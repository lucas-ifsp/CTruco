module domain {
    requires java.logging;

    exports com.bueno.domain.usecases.game to application.desktop, application.console, application.persistence;
    exports com.bueno.domain.usecases.player to application.persistence;
    exports com.bueno.domain.usecases.hand to application.desktop, application.console;
    exports com.bueno.domain.usecases.hand.validators to application.console, application.desktop;

    exports com.bueno.domain.entities.deck;
    exports com.bueno.domain.entities.player.util to application.desktop, application.console, application.persistence;
    exports com.bueno.domain.entities.player.dummybot to application.desktop, application.console;
    exports com.bueno.domain.entities.player.mineirobot to application.desktop, application.console;
    exports com.bueno.domain.entities.game to application.console, application.desktop, application.persistence;
}