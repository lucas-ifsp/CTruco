package com.bueno.application.main;

import com.bueno.application.main.commands.InitialMenuPrinter;
import com.bueno.application.main.commands.ExecuteMenu;

public class ConsoleStarter {
    public static void main(String[] args) {
        ConsoleStarter console = new ConsoleStarter();
        console.printInitialMenu();
        console.menu();
    }

    private void printInitialMenu(){
        InitialMenuPrinter init = new InitialMenuPrinter();
        init.execute();
    }

    private void menu(){
        ExecuteMenu printer = new ExecuteMenu();
        printer.execute();
    }


}
