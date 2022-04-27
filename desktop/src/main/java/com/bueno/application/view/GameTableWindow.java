/*
 *  Copyright (C) 2021 Lucas B. R. de Oliveira - IFSP/SCL
 *  Contact: lucas <dot> oliveira <at> ifsp <dot> edu <dot> br
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
 *  along with CTruco.  If not, see <https://www.gnu.org/licenses/>
 */

package com.bueno.application.view;

import com.bueno.application.controller.GameTableController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.LogManager;

public class GameTableWindow {

    public void show(String username, String botName) {
        LogManager.getLogManager().reset();
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane graph = loader.load(Objects.requireNonNull(getClass().getResource("game_table.fxml")).openStream());
            GameTableController controller = loader.getController();
            controller.createGame(username, botName);

            Scene scene = new Scene(graph, 1024, 740);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setOnCloseRequest(unused -> new LandingWindow().start(new Stage()));
            stage.setTitle("CTruco: Truco game for didactic purposes -- Developed with \u2665 by Prof. Lucas Oliveira");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
