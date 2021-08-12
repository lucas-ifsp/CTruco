package com.bueno.truco.application.desktop.view;

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

public class WindowTrucoResponse {

    private TrucoResponseController controller;

    public Integer showAndWait(String requesterName, int numberOfPoints) {
        try {
            FutureTask<Integer> futureTask = new FutureTask<>(createCallableDialog(requesterName, numberOfPoints));
            Platform.runLater(futureTask);
            return futureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Callable<Integer> createCallableDialog(final String requesterName, final int numberOfPoints) {
        Callable<Integer> callable = () -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                final Pane graph = loader.load(Objects.requireNonNull(getClass().getResource("truco_response.fxml")).openStream());
                Scene scene = new Scene(graph, 480, 165);
                controller = loader.getController();
                controller.configureViewInfo(requesterName, numberOfPoints);
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
        return callable;
    }
}
