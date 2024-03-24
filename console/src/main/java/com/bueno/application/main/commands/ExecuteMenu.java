package com.bueno.application.main.commands;

import com.bueno.application.utils.Command;
import com.bueno.application.withbots.features.EvaluateBot;
import com.bueno.application.withbots.features.PlayWithBots;
import com.bueno.application.withuser.PlayAgainstBots;

import java.util.Scanner;
import java.util.logging.LogManager;

public class ExecuteMenu implements Command<Void> {

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
        System.out.println("\nWhat do you Wanna Play");
        System.out.println("Player vs Bot...............[1]");
        System.out.println("Simulate Bot vs Bot match...[2]");
        System.out.println("Evaluate a Bot..............[3]");
        System.out.println("exit........................[0]");
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
                final var playBots = new PlayWithBots();
                playBots.playWithBotsConsole();
            }
            case "3" ->{
                final var evaluateBot = new EvaluateBot();
                evaluateBot.againstAll();
            }
            default -> System.out.println("invalid Answer! \n");

        }

    }
}
