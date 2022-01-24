module application.persistence {
    requires domain;

    exports com.bueno.persistence.inmemory to application.console, application.desktop;
}