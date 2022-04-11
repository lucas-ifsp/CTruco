module application.persistence {
    requires domain;
    requires lombok;
    requires java.persistence;
    requires spring.data.jpa;
    requires spring.context;
    exports com.bueno.persistence.inmemory;
}