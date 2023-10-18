import com.yuri.impl.BotMadeInDescalvado;

module bot.made.in.descalvado {
    requires bot.spi;
    exports com.yuri.impl;
    provides com.bueno.spi.service.BotServiceProvider with BotMadeInDescalvado;
}