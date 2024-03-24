package com.bueno.application.withbots.commands;

import com.bueno.application.utils.Command;

import java.util.List;

public class BotsAvailablePrinter implements Command<Void> {

    List<String> botNames;

    public BotsAvailablePrinter(List<String> botNames) {
        this.botNames = botNames;
    }

    @Override
    public Void execute() {
        for (int i = 0; i < botNames.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + botNames.get(i));
        }
        System.out.print("\n");
        return null;
    }

}
