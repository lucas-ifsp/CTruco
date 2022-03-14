import com.bueno.impl.mineirobot.MineiroBot;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.mineirobot;
    provides com.bueno.spi.service.BotServiceProvider with MineiroBot;
}