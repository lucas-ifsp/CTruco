module application.persistence {
    requires domain;
    requires lombok;
    requires spring.context;
    requires spring.beans;
    requires spring.data.mongodb;
    requires spring.data.commons;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.rmi;
    requires java.persistence;
    requires spring.data.jpa;
    requires java.naming;
    exports com.bueno.persistence.repositories;
    exports com.bueno.persistence.dao;
    exports com.bueno.persistence.dto;
    exports com.bueno.persistence;
}