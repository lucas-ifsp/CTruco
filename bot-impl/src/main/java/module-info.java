import com.bueno.impl.dummybot.DummyBot;
import com.indi.impl.addthenewsoul.AddTheNewSoul;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.dummybot;
    exports com.indi.impl.addthenewsoul;
    provides com.bueno.spi.service.BotServiceProvider with DummyBot, AddTheNewSoul;
}