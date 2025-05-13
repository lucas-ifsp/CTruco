import com.local.bueno.impl.dummybot.DummyBot;
import com.local.carvalho.candido.tomegapbot.GapBot;
import com.local.pedro.herick.skilldiffbot.SkillDiffBot;
import com.local.petrilli.sandro.malasiabot.MalasiaBot;
import com.local.aah.refactor.me.RefactorMePleaseBot;
import com.local.contiero.lemes.atrasabot.AtrasaBot;
import com.local.felipe.fabiano.truccard.Truccard;
import com.local.brito.macena.boteco.BotEco;
import com.local.abel.francisco.fogao6boca.Fogao6Boca;
import com.local.ghenrique.moedordecana.MoedorDeCana;

module bot.impl {
    requires bot.spi;
    requires java.compiler;
    requires java.smartcardio;
    requires jdk.jdi;
    requires domain;
    requires java.net.http;
    requires spring.webflux;
    requires reactor.core;
    requires com.google.gson;
    requires spring.web;
    requires spring.context;

    exports com.local.bueno.impl.dummybot;
    exports com.local.aah.refactor.me;
    exports com.local.contiero.lemes.atrasabot;
    exports com.local.felipe.fabiano.truccard;
    exports com.local.carvalho.candido.tomegapbot;
    exports com.local.brito.macena.boteco;
    exports com.local.ghenrique.moedordecana;
    exports com.local.pedro.herick.skilldiffbot;
    exports com.local.abel.francisco.fogao6boca;
    exports com.remote;

    provides com.bueno.spi.service.BotServiceProvider with
            DummyBot,
            RefactorMePleaseBot,
            MalasiaBot,
            AtrasaBot,
            Truccard,
            GapBot,
            BotEco,
            MoedorDeCana,
            Fogao6Boca,
            SkillDiffBot;
}
