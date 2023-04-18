import com.bueno.impl.dummybot.DummyBot;
import com.hermespiassi.casados.marrecobot.MarrecoBot;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.dummybot;
    exports com.hermespiassi.casados.marrecobot;
    provides com.bueno.spi.service.BotServiceProvider with DummyBot, MarrecoBot;
}