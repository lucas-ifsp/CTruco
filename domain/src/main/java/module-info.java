module domain {
    uses com.bueno.spi.service.BotServiceProvider;
    requires java.logging;
    requires bot.spi;
    requires bot.impl;
    requires spring.context;
    requires spring.beans;
    requires spring.webflux;
    requires com.google.gson;

    opens com.bueno.domain.usecases.bot.providers.remote to com.google.gson;
    exports com.bueno.domain.usecases.bot.providers.remote;

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
    exports com.bueno.domain.usecases.bot.providers.service;
}