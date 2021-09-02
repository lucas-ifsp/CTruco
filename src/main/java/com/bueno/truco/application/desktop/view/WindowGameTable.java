package com.bueno.truco.application.desktop.view;

import com.bueno.truco.application.desktop.controller.GameTableController;
import com.bueno.truco.domain.entities.player.DummyPlayer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.logging.LogManager;

public class WindowGameTable extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LogManager.getLogManager().reset();
        FXMLLoader loader = new FXMLLoader();
        Pane graph = loader.load(Objects.requireNonNull(getClass().getResource("game_table.fxml")).openStream());
        GameTableController controller = loader.getController();

        Scene scene = new Scene(graph, 1024, 768);
        stage.setScene(scene);
        stage.show();

        controller.setPlayers("Lucas", new DummyPlayer());

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                controller.startGame();
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
