module application.console {
    requires java.logging;
    requires domain;
    requires com.google.common;
    requires application.persistence;
    requires bot.impl;
    requires java.sql;
    requires bot.spi;
}