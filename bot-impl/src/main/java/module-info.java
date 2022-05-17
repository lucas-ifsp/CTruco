import com.bueno.impl.dummybot.DummyBot;
import com.bueno.impl.mineirobot.MineiroBot;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.mineirobot;
    exports com.bueno.impl.dummybot;
    provides com.bueno.spi.service.BotServiceProvider with MineiroBot, DummyBot;
}