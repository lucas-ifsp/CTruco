import com.local.bonelli.noli.paulistabot.PaulistaBot;
import com.local.brenoduda.cafeconlechebot.CafeConLecheBot;
import com.local.bueno.impl.dummybot.DummyBot;
import com.local.casal.impl.vapobot.VapoBot;
import com.local.caueisa.destroyerbot.DestroyerBot;
import com.local.cremonezzi.impl.carlsenbot.Carlsen;
import com.local.hermespiassi.casados.marrecobot.MarrecoBot;
import com.local.hideki.araujo.wrkncacnterbot.WrkncacnterBot;
import com.local.indi.impl.addthenewsoul.AddTheNewSoul;
import com.local.newton.dolensi.sabotabot.SabotaBot;
import com.local.zampieri.rissatti.impl.UncleBobBot.UncleBobBot;
import com.local.meima.skoltable.SkolTable;
import com.local.rossi.lopes.trucoguru.TrucoGuru;
import com.local.correacarini.impl.trucomachinebot.TrucoMachineBot;
import com.local.everton.ronaldo.arrebentabot.ArrebentaBot;
import com.local.almeida.strapasson.veiodobar.VeioDoBarBot;
import com.local.peixe.aguliari.perdenuncabot.PerdeNuncaBot;
import com.local.tatayrapha.leonardabot.LeonardaBot;
import com.local.murilos.aline.teconomarrecobot.TecoNoMarrecoBot;
import com.local.silvabrufato.impl.silvabrufatobot.SilvaBrufatoBot;
import com.local.yuri.impl.BotMadeInDescalvado;
import com.local.gatti.casaque.caipirasbot.CaipirasBot;
import com.local.gustavo.contiero.lazybot.LazyBot;

module bot.impl {
    requires bot.spi;
    requires java.net.http;
    requires spring.webflux;
    requires reactor.core;
    requires com.google.gson;
    exports com.local.bueno.impl.dummybot;
    exports com.local.indi.impl.addthenewsoul;
    exports com.local.hermespiassi.casados.marrecobot;
    exports com.local.cremonezzi.impl.carlsenbot;
    exports com.local.caueisa.destroyerbot;
    exports com.local.bonelli.noli.paulistabot;
    exports com.local.hideki.araujo.wrkncacnterbot;
    exports com.local.newton.dolensi.sabotabot;
    exports com.local.brenoduda.cafeconlechebot;
    exports com.local.meima.skoltable;
    exports com.local.rossi.lopes.trucoguru;
    exports com.local.almeida.strapasson.veiodobar;
    exports com.local.tatayrapha.leonardabot;
    exports com.local.murilos.aline.teconomarrecobot;
    exports com.local.silvabrufato.impl.silvabrufatobot;
    exports com.local.everton.ronaldo.arrebentabot;
    exports com.local.yuri.impl;
    exports com.local.gatti.casaque.caipirasbot;
    exports com.local.gustavo.contiero.lazybot;


    provides com.bueno.spi.service.BotServiceProvider with
            ArrebentaBot,
            TecoNoMarrecoBot,
            SilvaBrufatoBot,
            LeonardaBot,
            VeioDoBarBot,
            PerdeNuncaBot,
            TrucoMachineBot,
            BotMadeInDescalvado,
            TrucoGuru,
            SkolTable,
            UncleBobBot,
            CafeConLecheBot,
            VapoBot,
            SabotaBot,
            CaipirasBot,
            DummyBot,
            Carlsen,
            DestroyerBot,
            WrkncacnterBot,
            PaulistaBot,
            MarrecoBot,
            AddTheNewSoul,
            LazyBot;
}