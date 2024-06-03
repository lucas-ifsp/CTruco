package com.bueno.persistence;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseBuilder {

    public void buildDataBaseIfMissing(){
            System.out.println("Building tables if they don't exists: \n");
            buildTables();
    }

    private void buildTables(){
        try(Statement statement = ConnectionFactory.createStatement()) {
            statement.addBatch(createAppUserTable());
            statement.addBatch(createRemoteBotsTable());
            statement.addBatch(createGameResultTable());
            statement.addBatch(createHandResultsTable());
            statement.executeBatch();

            System.out.println("DATABASE CREATED");
        }catch (SQLException e) {
            System.err.println( e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String createAppUserTable(){
        return """
                CREATE TABLE IF NOT EXISTS APP_USER(
                    uuid VARCHAR(36) NOT NULL,
                    username TEXT NOT NULL,
                    email TEXT NOT NULL,
                    password TEXT NOT NULL,
                    CONSTRAINT uuid_pk PRIMARY KEY (uuid),
                    CONSTRAINT username_uk UNIQUE (username),
                    CONSTRAINT email_uk UNIQUE (email)
                );
                """;
    }

    private String createRemoteBotsTable(){
        return """
                CREATE TABLE IF NOT EXISTS REMOTE_BOT (
                    uuid VARCHAR(36) NOT NULL,
                    user_uuid VARCHAR(36) NOT NULL,
                    name TEXT NOT NULL,
                    url TEXT NOT NULL,
                    port TEXT NOT NULL,
                    PRIMARY KEY (uuid),
                    CONSTRAINT user_id_fk FOREIGN KEY (user_uuid) REFERENCES APP_USER(uuid),
                    CONSTRAINT name_uk UNIQUE (name),
                    CONSTRAINT url_port_uk UNIQUE (url,port)
                );
                """;
    }

    private String createGameResultTable(){
        return """
                CREATE TABLE IF NOT EXISTS GAME_RESULT(
                    game_uuid VARCHAR(36) NOT NULL,
                    game_start TIMESTAMP NOT NULL,
                    game_end TIMESTAMP,
                    winner_uuid VARCHAR(36),
                    player1_uuid VARCHAR(36) NOT NULL,
                    player1_score INTEGER,
                    player2_uuid VARCHAR(36) NOT NULL,
                    player2_score INTEGER,
                    CONSTRAINT game_uuid_pk PRIMARY KEY (game_uuid)
                );
                """;
    }

    private String createHandResultsTable(){
        return """
                CREATE TABLE IF NOT EXISTS HAND_RESULT(
                    id INTEGER NOT NULL,
                    r1_c1 VARCHAR(2),
                    r1_c2 VARCHAR(2),
                    r2_c1 VARCHAR(2),
                    r2_c2 VARCHAR(2),
                    r3_c1 VARCHAR(2),
                    r3_c2 VARCHAR(2),
                    game_uuid VARCHAR(36) NOT NULL,
                    hand_type VARCHAR(9) NOT NULL,
                    hand_winner VARCHAR(36),
                    points INTEGER NOT NULL,
                    points_proposal INTEGER,
                    r1_winner VARCHAR(36),
                    r2_winner VARCHAR(36),
                    r3_winner VARCHAR(36),
                    vira VARCHAR(2),
                    CONSTRAINT hand_results_id_pk PRIMARY KEY(id),
                    CONSTRAINT game_uuid_fk FOREIGN KEY (game_uuid)
                        REFERENCES GAME_RESULT(game_uuid)
                );
                """;
    }

}

