module domain {
    requires java.logging;

    exports com.bueno.domain.usecases.game to application.desktop, application.console;
    exports com.bueno.domain.usecases.hand to application.desktop, application.console;

    exports com.bueno.domain.entities.hand to application.desktop, application.console;
    exports com.bueno.domain.entities.deck to application.desktop, application.console;
    exports com.bueno.domain.entities.truco to application.desktop, application.console;
    exports com.bueno.domain.entities.player.util to application.desktop, application.console;
    exports com.bueno.domain.entities.player.dummybot to application.desktop, application.console;
    exports com.bueno.domain.entities.player.mineirobot to application.desktop, application.console;
    exports com.bueno.domain.entities.game to application.console;
}