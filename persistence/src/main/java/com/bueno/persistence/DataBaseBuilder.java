package com.bueno.persistence;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

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
            System.err.println( e.getClass() + ": " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
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
                    CONSTRAINT email_uk UNIQUE (email),
                    CONSTRAINT password_uk UNIQUE (password)
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
                    game_uuid TEXT NOT NULL,
                    game_start DATE NOT NULL,
                    game_end DATE,
                    winner_uuid VARCHAR(36),
                    player1_uuid VARCHAR(36) NOT NULL,
                    player1_score INTEGER,
                    player2_uuid VARCHAR(36) NOT NULL,
                    player2_score INTEGER,
                    CONSTRAINT game_uuid_pk PRIMARY KEY (game_uuid),
                    CONSTRAINT winner_uuid_fk_app_user FOREIGN KEY (winner_uuid)
                        REFERENCES APP_USER(uuid),
                    CONSTRAINT winner_uuid_fk_remote_bots FOREIGN KEY (winner_uuid)
                        REFERENCES REMOTE_BOT(uuid),
                    CONSTRAINT p1_uuid_fk_app_user FOREIGN KEY (player1_uuid)
                        REFERENCES APP_USER(uuid),
                    CONSTRAINT p1_uuid_fk_remote_bot FOREIGN KEY (player1_uuid)
                        REFERENCES REMOTE_BOT(uuid),
                    CONSTRAINT p2_uuid_fk_app_user FOREIGN KEY (player2_uuid)
                        REFERENCES APP_USER(uuid),
                    CONSTRAINT p2_uuid_fk_remote_bot FOREIGN KEY (player2_uuid)
                        REFERENCES REMOTE_BOT(uuid)
                );
                """;
    }

    private String createHandResultsTable(){
        return """
                CREATE TABLE IF NOT EXISTS HAND_RESULTS(
                    id INTEGER NOT NULL,
                    hand_type VARCHAR(9) NOT NULL,
                    game_uuid VARCHAR(36) NOT NULL,
                    hand_winner VARCHAR(36),
                    points INTEGER NOT NULL,
                    points_proposal INTEGER,
                    CONSTRAINT hand_results_id_pk PRIMARY KEY(id),
                    CONSTRAINT game_uuid_fk FOREIGN KEY (game_uuid)
                        REFERENCES GAME_RESULT(game_uuid)
                );
                """;
    }

}

