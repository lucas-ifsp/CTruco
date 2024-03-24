package com.bueno.application.withbots.commands;

import com.bueno.application.utils.Command;
import com.google.common.primitives.Ints;

import java.util.List;
import java.util.Scanner;

public class BotOptionReader implements Command<Integer> {

    List<String> botNames;

    public BotOptionReader(List<String> botNames) {
        this.botNames = botNames;
    }

    @Override
    public Integer execute() {
        Scanner scanner = new Scanner(System.in);
        Integer botNumber;
        while (true) {
            System.out.print("Select a bot by number: ");
            botNumber = Ints.tryParse(scanner.nextLine());
            if (botNumber == null || botNumber < 1 || botNumber > botNames.size()) {
                System.out.println("Invalid input!");
                continue;
            }
            break;
        }
        return botNumber;
    }
}
