import com.bueno.bots.mineirobot.MineiroBotService;
import com.bueno.domain.usecases.bot.BotService;

module botimpl {
    requires domain;
    provides BotService with MineiroBotService;
}