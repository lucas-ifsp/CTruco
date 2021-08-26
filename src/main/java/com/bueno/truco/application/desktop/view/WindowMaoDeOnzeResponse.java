package com.bueno.truco.application.desktop.view;

import com.bueno.truco.application.desktop.controller.MaoDeOnzeResponseController;
import com.bueno.truco.application.desktop.controller.TrucoResponseController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class WindowMaoDeOnzeResponse {

    private MaoDeOnzeResponseController controller;

    public boolean showAndWait() {
        try {
            FutureTask<Boolean> futureTask = new FutureTask<>(createCallableDialog());
            Platform.runLater(futureTask);
            return futureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Callable<Boolean> createCallableDialog() {
        return () -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                final Pane graph = loader.load(Objects.requireNonNull(getClass().getResource("mao_de_onze_response.fxml")).openStream());
                Scene scene = new Scene(graph, 480, 165);
                controller = loader.getController();

                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                return controller.getResult();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };
    }
}
