package com.bueno.application.withbots.commands;

import com.bueno.application.utils.Command;

public class WaitingMessagePrinter implements Command<Void> {

    @Override
    public Void execute() {
        System.out.println("Simulando Partidas...");
        System.out.println("Isso pode demorar um pouco.\n");
        return null;
    }
}
