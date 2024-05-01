module bot.spi {
    exports com.bueno.spi.service;
    exports com.bueno.spi.model;

    opens com.bueno.spi.model to com.google.gson, bot.impl;

    uses com.bueno.spi.service.BotServiceProvider;
}