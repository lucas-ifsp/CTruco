import com.bueno.impl.dummybot.DummyBot;
import com.hideki.araujo.wrkncacnterbot.WrkncacnterBot;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import com.caueisa.destroyerbot.DestroyerBot;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.dummybot;
    exports com.cremonezzi.impl.carlsenbot;
    provides com.bueno.spi.service.BotServiceProvider with DummyBot, Carlsen, DestroyerBot, WrkncacnterBot;
}