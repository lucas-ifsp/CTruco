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

package com.bueno.application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TrucoResponseController {

    @FXML public Button btnRaise;
    @FXML public Label txtQuestion;

    private int result;

    public void configureViewInfo(String requesterName, int numberOfPoints, boolean canRaise) {
        btnRaise.setDisable(!canRaise);
        txtQuestion.setText(requesterName + " está pedindo " + toPointsString(numberOfPoints) + ". Você deseja:");
        if(numberOfPoints == 12) {
            btnRaise.setText("--");
            btnRaise.setDisable(true);
            return;
        }
        btnRaise.setText(toPointsString(numberOfPoints + 3) + "!!!");
    }

    private String toPointsString(int points) {
        return switch (points) {
            case 3 -> "Truco";
            case 6 -> "Seis";
            case 9 -> "Nove";
            case 12 -> "Doze";
            default -> "";
        };
    }

    public void accept(ActionEvent actionEvent) {
        result = 0;
        close();
    }

    public void run(ActionEvent actionEvent) {
        result = -1;
        close();
    }

    public void rise(ActionEvent actionEvent) {
        result = 1;
        close();
    }

    private void close(){
        Stage stage = (Stage) btnRaise.getScene().getWindow();
        stage.close();
    }

    public int getResult() {
        return result;
    }
}
