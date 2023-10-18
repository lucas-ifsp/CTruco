import com.bonelli.noli.paulistabot.PaulistaBot;
import com.bueno.impl.dummybot.DummyBot;
import com.caueisa.destroyerbot.DestroyerBot;
import com.cremonezzi.impl.carlsenbot.Carlsen;
import com.everton.ronaldo.arrebentabot.ArrebentaBot;
import com.hermespiassi.casados.marrecobot.MarrecoBot;
import com.hideki.araujo.wrkncacnterbot.WrkncacnterBot;
import com.indi.impl.addthenewsoul.AddTheNewSoul;
import com.tatayrapha.leonardabot.LeonardaBot;
import com.murilos.aline.teconomarrecobot.TecoNoMarrecoBot;
import com.silvabrufato.impl.silvabrufatobot.SilvaBrufatoBot;

module bot.impl {
    requires bot.spi;
    exports com.bueno.impl.dummybot;
    exports com.indi.impl.addthenewsoul;
    exports com.hermespiassi.casados.marrecobot;
    exports com.cremonezzi.impl.carlsenbot;
    exports com.caueisa.destroyerbot;
    exports com.bonelli.noli.paulistabot;
    exports com.hideki.araujo.wrkncacnterbot;
    exports com.tatayrapha.leonardabot;
    exports com.murilos.aline.teconomarrecobot;
    exports com.silvabrufato.impl.silvabrufatobot;
    exports com.everton.ronaldo.arrebentabot;
    
    provides com.bueno.spi.service.BotServiceProvider with
            ArrebentaBot,
            TecoNoMarrecoBot,
            SilvaBrufatoBot,
            LeonardaBot,
            DummyBot, 
            Carlsen, 
            DestroyerBot, 
            WrkncacnterBot, 
            PaulistaBot, 
            MarrecoBot,  
            AddTheNewSoul;
