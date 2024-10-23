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
import com.belini.luciano.matapatobot.MataPatoBot;
import com.bruno.tiago.jeckiechanbot.JackieChanBot;
import com.carvalho.candido.tomegapbot.GapBot;
import com.castro.calicchio.jogasafebot.JogaSafeBot;
import com.francisco.bruno.pedrohenriquebot.PedroHenriqueBot;
import com.gabriel.kayky.coisaruim.CoisaRuim;
import com.garcia.orlandi.slayerbot.SlayerBot;
import com.erick.itaipavabot.ItaipavaBot;
import com.joao.alexandre.jormungandrbot.JormungandrBot;
import com.lucasmurilo.m.lazarinipodenciano.Akkosocorrompido;
import com.luigi.ana.batatafritadobarbot.BatataFritaDoBarBot;
import com.petrilli.sandro.malasiabot.MalasiaBot;
import com.soares.gibim.chatgptbot.ChatGptBot;
import com.pedrocagiovane.pauladasecabot.PauladaSecaBot;
import com.renato.DarthVader.DarthVader;
import com.fabio.bruno.minepowerbot.MinePowerBot;
import com.otavio.lopes.teitasbot.TeitasBot;
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
import com.belini.luciano.matapatobot.MataPatoBot;
import com.brito.macena.boteco.BotEco;
import com.adriann.emanuel.armageddon.Armageddon;
import com.Selin.Bonelli.zetruquero.Zetruquero;
import com.manhani.stefane.reimubot.ReimuBot;
import com.adivic.octopus.Octopus;
import com.eduardo.vinicius.camaleaotruqueiro.CamaleaoTruqueiro;
import com.bianca.joaopedro.lgtbot.Lgtbot;

module bot.impl {
    requires bot.spi;
    requires domain;

    requires java.net.http;
    requires spring.webflux;
    requires reactor.core;
    requires com.google.gson;
    requires spring.web;
    requires spring.context;

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

    exports com.belini.luciano.matapatobot;
    exports com.brito.macena.boteco;
    exports com.Selin.Bonelli.zetruquero;
    exports com.luigi.ana.batatafritadobarbot;
    exports com.manhani.stefane.reimubot;
    exports com.adivic.octopus;
    exports com.eduardo.vinicius.camaleaotruqueiro;
    exports com.bianca.joaopedro.lgtbot;
    exports com.adriann.emanuel.armageddon;
    exports com.remote;
    requires java.compiler;
    requires java.smartcardio;

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
            PedroHenriqueBot,
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
            MataPatoBot,
            BotEco,
            Armageddon,
            Zetruquero,
            BatataFritaDoBarBot,
            ReimuBot,
            Octopus,
            CamaleaoTruqueiro,
            ReiDoZap,
            Lgtbot;
}