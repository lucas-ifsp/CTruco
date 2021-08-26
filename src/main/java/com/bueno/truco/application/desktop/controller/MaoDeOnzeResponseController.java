package com.bueno.truco.application.desktop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MaoDeOnzeResponseController {

    @FXML private Button btnRun;

    private boolean result;

    public void accept(ActionEvent e) {
        result = true;
        close();
    }

    public void run(ActionEvent e) {
        result = false;
        close();
    }

    private void close(){
        Stage stage = (Stage) btnRun.getScene().getWindow();
        stage.close();
    }

    public boolean getResult() {
        return result;
    }
}
