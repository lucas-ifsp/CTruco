module domain {
    uses com.bueno.spi.service.BotServiceProvider;
    requires java.logging;
    requires bot.spi;
    requires spring.context;
    requires spring.beans;

    exports com.bueno.domain.usecases.game.usecase;
    exports com.bueno.domain.usecases.game.dtos;
    exports com.bueno.domain.usecases.user;
    exports com.bueno.domain.usecases.user.dtos;
    exports com.bueno.domain.usecases.intel;
    exports com.bueno.domain.usecases.intel.dtos;
    exports com.bueno.domain.usecases.hand;
    exports com.bueno.domain.usecases.utils.exceptions;
    exports com.bueno.domain.usecases.hand.dtos;
    exports com.bueno.domain.usecases.game.repos;
    exports com.bueno.domain.usecases.bot.repository;
    exports com.bueno.domain.usecases.bot.providers;
    exports com.bueno.domain.usecases.bot.usecase;
    exports com.bueno.domain.usecases.bot.dtos;
    exports com.bueno.domain.usecases.tournament.usecase;
    exports com.bueno.domain.usecases.tournament.repos;
    exports com.bueno.domain.usecases.tournament.dtos;
    exports com.bueno.domain.usecases.tournament.converter;
}