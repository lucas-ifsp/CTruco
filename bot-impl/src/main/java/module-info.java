import com.bonelli.noli.paulistabot.PaulistaBot;
import com.brenoduda.cafeconlechebot.CafeConLecheBot;
import com.bueno.impl.dummybot.DummyBot;
import com.casal.impl.vapobot.VapoBot;
import com.castro.calicchio.jogasafebot.JogaSafeBot;
import com.caueisa.destroyerbot.DestroyerBot;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import com.garcia.orlandi.slayerbot.SlayerBot;
import com.erick.itaipavabot.ItaipavaBot;
import com.hermespiassi.casados.marrecobot.MarrecoBot;
import com.hideki.araujo.wrkncacnterbot.WrkncacnterBot;
import com.indi.impl.addthenewsoul.AddTheNewSoul;
import com.joao.alexandre.jormungandrbot.JormungandrBot;
import com.lucasmurilo.m.lazarinipodenciano.Akkosocorrompido;
import com.petrilli.sandro.malasiabot.MalasiaBot;
import com.newton.dolensi.sabotabot.SabotaBot;
import com.soares.gibim.chatgptbot.ChatGptBot;
import com.pedrocagiovane.pauladasecabot.PauladaSecaBot;
import com.renato.DarthVader.DarthVader;
import com.zampieri.rissatti.impl.UncleBobBot.UncleBobBot;
import com.meima.skoltable.SkolTable;
import com.rossi.lopes.trucoguru.TrucoGuru;
import com.correacarini.impl.trucomachinebot.TrucoMachineBot;
import com.everton.ronaldo.arrebentabot.ArrebentaBot;
import com.almeida.strapasson.veiodobar.VeioDoBarBot;
import com.peixe.aguliari.perdenuncabot.PerdeNuncaBot;
import com.tatayrapha.leonardabot.LeonardaBot;
import com.murilos.aline.teconomarrecobot.TecoNoMarrecoBot;
import com.silvabrufato.impl.silvabrufatobot.SilvaBrufatoBot;
import com.yuri.impl.BotMadeInDescalvado;
import com.gatti.casaque.caipirasbot.CaipirasBot;
import com.gustavo.contiero.lazybot.LazyBot;
import com.shojisilva.fernasbot.FernasBot;
import com.fabio.bruno.minepowerbot.MinePowerBot;
import com.otavio.lopes.teitasbot.TeitasBot;
import com.miguelestevan.jakaredumatubot.JakareDuMatuBot;
import com.barbara.lucasCruz.patriciaAparecida.PatriciaAparecida;
import com.Sigoli.Castro.PatoBot.PatoBot;
import com.antonelli.rufino.bardoalexbot.BarDoAlexBot;
import com.contiero.lemes.atrasabot.AtrasaBot;
import com.lucas.felipe.newbot.NewBot;
import com.castro.calicchio.jogasafebot.JogaSafeBot;

module bot.impl {
    requires bot.spi;
    requires java.compiler;
    requires java.smartcardio;
    exports com.bueno.impl.dummybot;
    exports com.indi.impl.addthenewsoul;
    exports com.hermespiassi.casados.marrecobot;
    exports com.cremonezzi.impl.carlsenbot;
    exports com.caueisa.destroyerbot;
    exports com.bonelli.noli.paulistabot;
    exports com.hideki.araujo.wrkncacnterbot;
    exports com.newton.dolensi.sabotabot;
    exports com.pedrocagiovane.pauladasecabot;
    exports com.brenoduda.cafeconlechebot;
    exports com.meima.skoltable;
    exports com.rossi.lopes.trucoguru;
    exports com.almeida.strapasson.veiodobar;
    exports com.tatayrapha.leonardabot;
    exports com.murilos.aline.teconomarrecobot;
    exports com.silvabrufato.impl.silvabrufatobot;
    exports com.everton.ronaldo.arrebentabot;
    exports com.yuri.impl;
    exports com.gatti.casaque.caipirasbot;
    exports com.gustavo.contiero.lazybot;
    exports com.fabio.bruno.minepowerbot;
    exports com.joao.alexandre.jormungandrbot;
    exports com.lucasmurilo.m.lazarinipodenciano;
    exports com.otavio.lopes.teitasbot;
    exports com.miguelestevan.jakaredumatubot;
    exports com.soares.gibim.chatgptbot;
    exports com.barbara.lucasCruz.patriciaAparecida;
    exports com.Sigoli.Castro.PatoBot;
    exports com.garcia.orlandi.slayerbot;
    exports com.erick.itaipavabot;
    exports com.renato.DarthVader;
    exports com.antonelli.rufino.bardoalexbot;
    exports com.lucas.felipe.newbot;
    exports com.contiero.lemes.atrasabot;
    exports com.shojisilva.fernasbot;
    exports com.castro.calicchio.jogasafebot;

    provides com.bueno.spi.service.BotServiceProvider with
            DummyBot,
            Carlsen,
            DestroyerBot,
            WrkncacnterBot,
            PaulistaBot,
            MarrecoBot,
            AddTheNewSoul,
            ArrebentaBot,
            BarDoAlexBot,
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
            LazyBot,
            PauladaSecaBot,
            MinePowerBot,
            JormungandrBot,
            Akkosocorrompido,
            JakareDuMatuBot,
            ChatGptBot,
            PatriciaAparecida,
            PatoBot,
            ItaipavaBot,
            DarthVader,
            SlayerBot,
            TeitasBot,
            MalasiaBot,
            FernasBot,
            NewBot,
            AtrasaBot,
            JogaSafeBot;
}