module application.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires domain;

    opens com.bueno.application.controller to javafx.fxml;
    exports com.bueno.application.view to javafx.graphics;
}