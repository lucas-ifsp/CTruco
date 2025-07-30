/*
 *  Copyright (C) 2022 Lucas B. R. de Oliveira - IFSP/SCL
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

import com.bueno.application.view.GameTableWindow;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.bot.repository.RemoteBotRepository;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.persistence.repositories.RemoteBotRepositoryImpl;
import com.bueno.persistence.repositories.UserRepositoryImpl;
import com.remote.RemoteBotApiAdapter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class LandingController implements ChangeListener<Boolean> {

    public static final boolean ON_FOCUS = true;
    @FXML
    private TextField txtPlayer;
    @FXML
    private ComboBox<String> cbBot;

    public String txtDefaultStyle;

    @FXML
    private void initialize() {
        RemoteBotRepository botRepository = new RemoteBotRepositoryImpl();
        RemoteBotApi botApi = new RemoteBotApiAdapter();
        BotManagerService botManagerService = new BotManagerService(botRepository, botApi);
        final List<String> bots = botManagerService.providersNames();
        cbBot.setItems(FXCollections.observableList(bots));
        cbBot.getSelectionModel().select(0);
        txtPlayer.focusedProperty().addListener(this);
        txtDefaultStyle = txtPlayer.getStyle();
    }

    public void play(ActionEvent actionEvent) {
        final var player = txtPlayer.getText().trim();
        final var bot = cbBot.getSelectionModel().getSelectedItem();

        if (isNotValid(player)) return;

        final var window = new GameTableWindow();
        window.show(player, bot);
        closeWindow();
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if (oldValue == null || newValue == oldValue) return;
        if (newValue == ON_FOCUS) {
            txtPlayer.setStyle(txtDefaultStyle);
            return;
        }
        if (isNotValid(txtPlayer.getText().trim()))
            txtPlayer.setStyle("-fx-background-color: #ecd3d5;");
    }

    private boolean isNotValid(String username) {
        return (username == null || username.length() < 2 || username.length() > 20);
    }

    public void close(ActionEvent actionEvent) {
        closeWindow();
    }

    private void closeWindow() {
        final Stage stage = (Stage) txtPlayer.getScene().getWindow();
        stage.close();
    }
}
