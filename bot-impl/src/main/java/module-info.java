
import com.candido.tomegapbot.GapBot;
import com.pedro.herick.skilldiffbot.SkillDiffBot;
import com.petrilli.sandro.malasiabot.MalasiaBot;
import com.contiero.lemes.atrasabot.AtrasaBot;
import com.felipe.fabiano.truccard.Truccard;
import com.brito.macena.boteco.BotEco;
import com.abel.francisco.fogao6boca.Fogao6Boca;
import com.ghenrique.moedordecana.MoedorDeCana;

module bot.impl {
    requires bot.spi;
    requires java.compiler;
    requires java.smartcardio;
    requires jdk.jdi;

    requires domain;

    requires java.net.http;
    requires com.google.gson;
    requires spring.webflux;
    requires reactor.core;
    exports com.local.bueno.impl.dummybot;
    exports com.contiero.lemes.atrasabot;
    exports com.felipe.fabiano.truccard;
    exports com.carvalho.candido.tomegapbot;
    exports com.brito.macena.boteco;
    exports com.ghenrique.moedordecana;
    exports com.pedro.herick.skilldiffbot;
    exports com.abel.francisco.fogao6boca;

    provides com.bueno.spi.service.BotServiceProvider with
            DummyBot,
            MalasiaBot,
            AtrasaBot,
            Truccard,
            GapBot,
            BotEco,
            MoedorDeCana,
            Fogao6Boca,
            SkillDiffBot;
}