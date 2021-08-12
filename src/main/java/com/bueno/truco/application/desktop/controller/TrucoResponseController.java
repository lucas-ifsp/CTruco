package com.bueno.truco.application.desktop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TrucoResponseController {

    @FXML public Button btnRise;
    @FXML public Label txtQuestion;

    private int result;

    public void configureViewInfo(String requesterName, int numberOfPoints) {
        txtQuestion.setText(requesterName + " está pedindo " + toPointsString(numberOfPoints) + ". Você deseja:");
        if(numberOfPoints == 12) {
            btnRise.setText("--");
            btnRise.setDisable(true);
            return;
        }
        btnRise.setText(toPointsString(numberOfPoints + 3) + "!!!");
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
        Stage stage = (Stage) btnRise.getScene().getWindow();
        stage.close();
    }

    public int getResult() {
        return result;
    }
}
