import com.almeida.strapasson.veiodobar.VeioDoBarBot;
import com.bonelli.noli.paulistabot.PaulistaBot;
import com.brenoduda.cafeconlechebot.CafeConLecheBot;
import com.bruno.tiago.jeckiechanbot.JackieChanBot;
import com.bueno.impl.dummybot.DummyBot;
import com.carvalho.candido.tomegapbot.GapBot;
import com.casal.impl.vapobot.VapoBot;
import com.castro.calicchio.jogasafebot.JogaSafeBot;
import com.caueisa.destroyerbot.DestroyerBot;
import com.correacarini.impl.trucomachinebot.TrucoMachineBot;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import com.gabriel.kayky.coisaruim.CoisaRuim;
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
import com.murilos.aline.teconomarrecobot.TecoNoMarrecoBot;
import com.peixe.aguliari.perdenuncabot.PerdeNuncaBot;
import com.rossi.lopes.trucoguru.TrucoGuru;
import com.silvabrufato.impl.silvabrufatobot.SilvaBrufatoBot;
import com.tatayrapha.leonardabot.LeonardaBot;
import com.yuri.impl.BotMadeInDescalvado;
import com.gatti.casaque.caipirasbot.CaipirasBot;
import com.gustavo.contiero.lazybot.LazyBot;
import com.fabio.bruno.minepowerbot.MinePowerBot;
import com.otavio.lopes.teitasbot.TeitasBot;
import com.everton.ronaldo.arrebentabot.ArrebentaBot;
import com.miguelestevan.jakaredumatubot.JakareDuMatuBot;
import com.barbara.lucasCruz.patriciaAparecida.PatriciaAparecida;
import com.Sigoli.Castro.PatoBot.PatoBot;
import com.antonelli.rufino.bardoalexbot.BarDoAlexBot;
import com.shojisilva.fernasbot.FernasBot;
import com.motta.impl.beepbot.BeepBot;
import com.contiero.lemes.atrasabot.AtrasaBot;
import com.lucas.felipe.newbot.NewBot;
import com.felipe.fabiano.truccard.Truccard;
import com.murilo.joao.jackbot.JackBot;
import com.luna.jundi.jokerBot.JokerBot;
import com.alanIan.casinhadecabloco.CasinhaDeCabloco;
import com.bernardo.caio.zeusbot.Zeusbot;
import com.campos.turazzi.reidozap.ReiDoZap;
import com.macena.mauricio.boteco.BotEco;

module bot.impl {
    requires bot.spi;
    requires java.compiler;
    requires java.smartcardio;

    exports com.bueno.impl.dummybot;
    exports com.indi.impl.addthenewsoul;
    exports com.hermespiassi.casados.marrecobot;
    exports com.newton.dolensi.sabotabot;
    exports com.cremonezzi.impl.carlsenbot;
    exports com.caueisa.destroyerbot;
    exports com.bonelli.noli.paulistabot;
    exports com.hideki.araujo.wrkncacnterbot;
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
    exports com.felipe.fabiano.truccard;
    exports com.gabriel.kayky.coisaruim;
    exports com.murilo.joao.jackbot;
    exports com.luna.jundi.jokerBot;
    exports com.carvalho.candido.tomegapbot;
    exports com.bruno.tiago.jeckiechanbot;
    exports com.alanIan.casinhadecabloco;
    exports com.bernardo.caio.zeusbot;
    exports com.motta.impl.beepbot;
    exports com.campos.turazzi.reidozap;
    exports com.macena.mauricio.boteco;

    provides com.bueno.spi.service.BotServiceProvider with
            ArrebentaBot,
            AddTheNewSoul,
            WrkncacnterBot,
            MarrecoBot,
            Carlsen,
            DummyBot,
            DestroyerBot,
            PaulistaBot,
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
            JogaSafeBot,
            Truccard,
            CoisaRuim,
            JackBot,
            JokerBot,
            GapBot,
            JackieChanBot,
            CasinhaDeCabloco,
            BeepBot,
            Zeusbot,
            ReiDoZap,
            BotEco;

}