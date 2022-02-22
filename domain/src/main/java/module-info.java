import com.bueno.domain.usecases.bot.impl.MineiroBot;

module domain {
    requires java.logging;

    exports com.bueno.domain.entities.deck;
    exports com.bueno.domain.entities.player.util;
    exports com.bueno.domain.entities.game;

    exports com.bueno.domain.usecases.utils;
    exports com.bueno.domain.usecases.game;
    exports com.bueno.domain.usecases.player;
    exports com.bueno.domain.usecases.hand.usecases;
    exports com.bueno.domain.usecases.bot.spi;
    exports com.bueno.domain.usecases.hand.validators;

    provides com.bueno.domain.usecases.bot.spi.BotServiceProvider with MineiroBot;
    uses com.bueno.domain.usecases.bot.spi.BotServiceProvider;
}