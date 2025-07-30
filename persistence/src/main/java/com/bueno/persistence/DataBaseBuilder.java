package com.bueno.persistence;

import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseBuilder {

    public void buildDataBaseIfMissing() throws SQLException {
        System.out.println("Building tables if they don't exists: \n");
        dropDatabases();

        try (Statement statement = ConnectionFactory.createStatement()) {
            statement.addBatch(createAppUserTable());
            statement.addBatch(createRemoteBotsTable());
            statement.addBatch(createGameResultTable());
            statement.addBatch(createHandResultsTable());
            statement.addBatch(createRankBotsTable());
//            statement.addBatch(createTournamentTable());
//            statement.addBatch(createTournamentParticipantsTable());
//            statement.addBatch(createTournamentMatchesTable());
//            statement.addBatch(createTournamentMatchTable());
            statement.executeBatch();

            System.out.println("DATABASE CREATED");
        } catch (SQLException e) {
            System.err.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void dropDatabases() throws SQLException {
        try (Statement statement = ConnectionFactory.createStatement()) {
            statement.addBatch("DROP TABLE IF EXISTS remote_bot");
            statement.addBatch("DROP TABLE IF EXISTS hand_result");
            statement.addBatch("DROP TABLE IF EXISTS game_result");
            statement.addBatch("DROP TABLE IF EXISTS app_user");
//            statement.addBatch("DROP TABLE IF EXISTS tournament");
//            statement.addBatch("DROP TABLE IF EXISTS tournament_participant");
//            statement.addBatch("DROP TABLE IF EXISTS tournament_match");
//            statement.addBatch("DROP TABLE IF EXISTS matches");
//            statement.addBatch("DROP TABLE IF EXISTS bot_rank");
            statement.executeBatch();
        }
    }

    private String createAppUserTable() {
        return """
                CREATE TABLE IF NOT EXISTS APP_USER(
                    uuid UUID NOT NULL,
                    username TEXT NOT NULL,
                    email TEXT NOT NULL,
                    password TEXT NOT NULL,
                    CONSTRAINT user_uuid_pk PRIMARY KEY (uuid),
                    CONSTRAINT username_uk UNIQUE (username),
                    CONSTRAINT email_uk UNIQUE (email)
                );
                """;
    }

    private String createRemoteBotsTable() {
        return """
                CREATE TABLE IF NOT EXISTS REMOTE_BOT (
                    uuid UUID NOT NULL,
                    user_uuid UUID NOT NULL,
                    name TEXT NOT NULL,
                    url TEXT NOT NULL,
                    port TEXT NOT NULL,
                    repository_url TEXT NOT NULL,
                    authorized BOOLEAN DEFAULT FALSE,
                    CONSTRAINT remote_bot_uuid_pk PRIMARY KEY (uuid),
                    CONSTRAINT user_id_fk FOREIGN KEY (user_uuid) REFERENCES APP_USER(uuid),
                    CONSTRAINT name_uk UNIQUE (name),
                    CONSTRAINT url_port_uk UNIQUE (url,port),
                    CONSTRAINT remote_bot_repository_url_uk UNIQUE (repository_url)
                );
                """;
    }

    private String createGameResultTable() {
        return """
                CREATE TABLE IF NOT EXISTS GAME_RESULT(
                    game_uuid UUID NOT NULL,
                    game_start TIMESTAMP NOT NULL,
                    game_end TIMESTAMP,
                    winner_uuid UUID,
                    player1_uuid UUID NOT NULL,
                    player1_score INTEGER,
                    player2_uuid UUID NOT NULL,
                    player2_score INTEGER,
                    CONSTRAINT game_uuid_pk PRIMARY KEY (game_uuid)
                );
                """;
    }

    private String createHandResultsTable() {
        return """
                CREATE TABLE IF NOT EXISTS HAND_RESULT(
                    id SERIAL,
                    r1_c1 VARCHAR(2),
                    r1_c2 VARCHAR(2),
                    r2_c1 VARCHAR(2),
                    r2_c2 VARCHAR(2),
                    r3_c1 VARCHAR(2),
                    r3_c2 VARCHAR(2),
                    game_uuid UUID NOT NULL,
                    hand_type VARCHAR(9) NOT NULL,
                    hand_winner UUID,
                    points INTEGER NOT NULL,
                    points_proposal INTEGER,
                    r1_winner UUID,
                    r2_winner UUID,
                    r3_winner UUID,
                    vira VARCHAR(2),
                    CONSTRAINT hand_results_id_pk PRIMARY KEY(id)
                );
                """;
    }

    private String createRankBotsTable() {
        return """
                CREATE TABLE IF NOT EXISTS BOT_RANK(
                    rank INTEGER,
                    bot_name VARCHAR(30),
                    wins INTEGER,
                    CONSTRAINT bot_rank_pk PRIMARY KEY(rank)
                );
                """;
    }

//    private String createTournamentTable() {
//        return """
//                CREATE TABLE IF NOT EXISTS Tournament(
//                    uuid UUID NOT NULL,
//                    size INTEGER NOT NULL,
//                    CONSTRAINT tournament_pk PRIMARY KEY (uuid)
//                );
//                """;
//    }

//    private String createTournamentParticipantsTable() {
//        return """
//                CREATE TABLE IF NOT EXISTS Tournament_participant(
//                    participant_name VARCHAR(30) NOT NULL,
//                    tournament_uuid UUID NOT NULL,
//                    CONSTRAINT tournament_participants_pk PRIMARY KEY (participant_name, tournament_uuid)
//                );
//                """;
//    }
//
//    private String createTournamentMatchesTable() {
//        return """
//                CREATE TABLE IF NOT EXISTS Tournament_match(
//                    tournament_uuid UUID NOT NULL,
//                    match_uuid UUID NOT NULL,
//                    CONSTRAINT tournament_matches_pk PRIMARY KEY (tournament_uuid, match_uuid)
//                );
//                """;
//    }
//
//    private String createTournamentMatchTable() {
//        return """
//                CREATE TABLE IF NOT EXISTS matches(
//                    uuid UUID NOT NULL,
//                    p1_name VARCHAR(30),
//                    p2_name VARCHAR(30),
//                    available BOOLEAN NOT NULL,
//                    winner_name VARCHAR(30),
//                    p1_score INTEGER NOT NULL,
//                    p2_score INTEGER NOT NULL,
//                    uuid_next UUID,
//                    CONSTRAINT tournament_match_pk PRIMARY KEY (uuid)
//                );
//                """;
//    }

}

