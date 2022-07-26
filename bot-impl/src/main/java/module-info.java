import com.bueno.impl.dummybot.DummyBot;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.dummybot;
    provides com.bueno.spi.service.BotServiceProvider with DummyBot;
}