module application.persistence {
    requires domain;
    requires lombok;
    requires java.persistence;
    requires spring.data.jpa;
    requires spring.context;
    requires spring.beans;
    requires spring.data.mongodb;
    requires spring.data.commons;
    exports com.bueno.persistence.repositories;
    exports com.bueno.persistence.dao;
    exports com.bueno.persistence.dto;
}