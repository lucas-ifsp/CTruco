/*
 * Copyright (C) 2021 Lucas B. R. de Oliveira
 *
 *  This file is part of CTruco (Truco game for didactic purpose).
 *
 *  CTruco is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CTruco is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <https://www.gnu.org/licenses/>
 */

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
                stage.setResizable(false);
                stage.showAndWait();
                return controller.getResult();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        };
    }
}
