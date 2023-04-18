import com.bueno.impl.dummybot.DummyBot;
import com.cremonezzi.impl.carlsenbot.Carlsen;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.dummybot;
    exports com.cremonezzi.impl.carlsenbot;
    provides com.bueno.spi.service.BotServiceProvider with DummyBot, Carlsen;
}