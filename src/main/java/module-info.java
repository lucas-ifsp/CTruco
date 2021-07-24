module desktop_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.boot;
    requires spring.boot.autoconfigure;

    opens com.bueno.truco.application.desktop.controller to javafx.fxml;

    exports com.bueno.truco.application.desktop.view to javafx.graphics;
}