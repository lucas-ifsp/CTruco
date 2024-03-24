package com.bueno.application.withbots.commands;

import com.bueno.application.utils.Command;

import java.util.Scanner;

public class NumberOfSimulationsReader implements Command<Integer> {

    @Override
    public Integer execute() {
        final var scanner = new Scanner(System.in);
        System.out.print("Number of simulations: ");
        final int times = scanner.nextInt();
        System.out.println("\nStarting simulation... it may take a while: ");
        return times;
    }
}
