package com.bueno.application.main.commands;

import com.bueno.application.utils.Command;

public class InitialMenuPrinter implements Command<Void> {
    @Override
    public Void execute() {
        System.out.println("=+=+= CTRUCO COSOLE =+=+=");
        return null;
    }
}
