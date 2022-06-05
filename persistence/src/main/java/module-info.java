module application.persistence {
    requires domain;
    requires lombok;
    requires java.persistence;
    requires spring.data.jpa;
    requires spring.context;
    requires spring.beans;
    exports com.bueno.persistence.mysql.repositories;
    exports com.bueno.persistence.mysql.dao;
    exports com.bueno.persistence.mysql.dto;
}