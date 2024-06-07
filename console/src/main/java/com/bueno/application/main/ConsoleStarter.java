package com.bueno.application.main;

import com.bueno.application.main.commands.InitialMenuPrinter;
import com.bueno.application.main.commands.ExecuteMenu;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.persistence.DataBaseBuilder;
import com.bueno.persistence.daoimpl.RemoteBotDaoImpl;
import com.bueno.persistence.daoimpl.UserDaoImpl;
import com.bueno.persistence.repositories.RemoteBotRepositoryImpl;
import com.bueno.persistence.repositories.UserRepositoryImpl;
import com.remote.RemoteBotApiAdapter;

import java.sql.SQLException;

public class ConsoleStarter {

    public static void main(String[] args) throws SQLException {
        DataBaseBuilder dataBaseBuilder = new DataBaseBuilder();
        dataBaseBuilder.buildDataBaseIfMissing();
        ConsoleStarter console = new ConsoleStarter();
        console.printInitialMenu();
        console.menu();
    }

    private void printInitialMenu() {
        InitialMenuPrinter init = new InitialMenuPrinter();
        init.execute();
    }

    private void menu() {
        UserRepository userRepository = new UserRepositoryImpl(new UserDaoImpl());
        RemoteBotRepositoryImpl RemoteBotRepository = new RemoteBotRepositoryImpl(new RemoteBotDaoImpl(), userRepository);
        ExecuteMenu printer = new ExecuteMenu(RemoteBotRepository, new RemoteBotApiAdapter());
        printer.execute();
    }
}
