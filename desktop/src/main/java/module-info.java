module application.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires domain;
    requires application.persistence;
    requires bot.impl;

    opens com.bueno.application.controller to javafx.fxml;
    exports com.bueno.application.view to javafx.graphics;
}