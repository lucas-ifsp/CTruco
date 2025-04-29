package com.bueno.application.main.commands;

import com.bueno.application.utils.Command;
import com.bueno.application.withbots.features.EvaluateBot;
import com.bueno.application.withbots.features.PlayWithBots;
import com.bueno.application.withbots.features.RankBots;
import com.bueno.application.withuser.PlayAgainstBots;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.persistence.repositories.RemoteBotRepositoryImpl;
import com.remote.RemoteBotApiAdapter;

import java.util.Scanner;
import java.util.logging.LogManager;

public class ExecuteMenu implements Command<Void> {

    private final RemoteBotRepository repository;
    private final RemoteBotApi botApi;

    public ExecuteMenu(RemoteBotRepository repository, RemoteBotApi botApi) {
        this.repository = repository;
        this.botApi = botApi;
    }

    @Override
    public Void execute() {
        Scanner scanner = new Scanner(System.in);
        String option = "";
        while (! option.equals("0")){
            menuOptions();
            option = scanner.nextLine().trim();
            menuSwitch(option);
        }
        return null;
    }
    private void menuOptions(){
        System.out.println("Play Player vs Bot...............[1]");
        System.out.println("Simulate Bot vs Bot .............[2]");
        System.out.println("Evaluate a Bot...................[3]");
        System.out.println("Rank all Bots....................[4]");
        System.out.println("Exit.............................[0]");
        System.out.print("Choose a menu option: ");
    }
    private void menuSwitch(String  option){
        switch (option){
            case "0" ->{}
            case "1" -> {
                LogManager.getLogManager().reset();
                final var cli = new PlayAgainstBots();
                cli.gameCLIStarter();
            }
            case "2" ->{
                final var playBots = new PlayWithBots(repository, botApi);
                playBots.playWithBotsConsole();
            }
            case "3" ->{
                final var evaluateBot = new EvaluateBot(repository,botApi);
                evaluateBot.againstAll();
            }
            case"4"->{
                final var rank = new RankBots(repository,botApi);
                rank.allBots();
            }
            default -> System.out.println("invalid Answer! \n");

        }

    }
}
