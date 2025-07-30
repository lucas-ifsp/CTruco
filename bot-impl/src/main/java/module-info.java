import com.bueno.impl.dummybot.DummyBot;
import com.carvalho.candido.tomegapbot.GapBot;
import com.pedro.herick.skilldiffbot.SkillDiffBot;
import com.petrilli.sandro.malasiabot.MalasiaBot;
import com.abel.francisco.fogao6boca.Fogao6Boca;
import com.aah.refactor.me.RefactorMePleaseBot;
import com.contiero.lemes.atrasabot.AtrasaBot;
import com.felipe.fabiano.truccard.Truccard;
import com.brito.macena.boteco.BotEco;
import com.ghenrique.moedordecana.MoedorDeCana;

module bot.impl {
    requires bot.spi;
    requires java.compiler;
    requires java.smartcardio;
    requires jdk.jdi;

    exports com.abel.francisco.fogao6boca;
    exports com.bueno.impl.dummybot;
    exports com.aah.refactor.me;
    exports com.contiero.lemes.atrasabot;
    exports com.felipe.fabiano.truccard;
    exports com.carvalho.candido.tomegapbot;
    exports com.brito.macena.boteco;
    exports com.ghenrique.moedordecana;
    exports com.pedro.herick.skilldiffbot;

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
