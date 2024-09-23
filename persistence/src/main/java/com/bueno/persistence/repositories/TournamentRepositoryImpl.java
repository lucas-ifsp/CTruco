package com.bueno.persistence.repositories;

import com.bueno.domain.usecases.tournament.dtos.TournamentDTO;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.persistence.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TournamentRepositoryImpl implements TournamentRepository {
    @Override
    public Optional<TournamentDTO> findTournamentById(UUID uuid) throws SQLException {

        String sql = "SELECT t.size, FROM tournaments t WHERE uuid = ?";
        List<String> participants;
        List<UUID> tournamentMatchUUID;
        int tournamentSize;

        tournamentSize = getTournamentSize(sql);
        participants = getParticipants();
        tournamentMatchUUID = getMatchesUUID();

        return Optional.of(new TournamentDTO(uuid,
                participants,
                tournamentMatchUUID,
                tournamentSize
        ));

    }

    private int getTournamentSize(String sql) throws SQLException {
        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setObject(1, UUID.class);
            ResultSet res = statement.executeQuery(sql);
            return res.getInt("size");
        }
    }

    private List<String> getParticipants() throws SQLException {
        String sql = "SELECT * FROM tournament_match WHERE tournament_uuid = ?";
        List<String> participantsMatchUUID = new ArrayList<>();

        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setObject(1, UUID.class);
            ResultSet res = statement.executeQuery(sql);
            while (res.next()) {
                participantsMatchUUID.add(res.getString("participant_name"));
            }
            return participantsMatchUUID;
        }

    }

    private List<UUID> getMatchesUUID() throws SQLException {
        String sql = "SELECT * FROM tournament_match WHERE tournament_uuid = ?";
        List<UUID> tournamentMatchesUUID = new ArrayList<>();

        try (PreparedStatement statement = ConnectionFactory.createPreparedStatement(sql)) {
            statement.setObject(1, UUID.class);
            ResultSet res = statement.executeQuery(sql);
            while (res.next()) {
                tournamentMatchesUUID.add(res.getObject("tournament_uuid", UUID.class));
            }
            return tournamentMatchesUUID;
        }

    }

    @Override
    public Map<UUID, TournamentDTO> getTournaments() {
        return Map.of();
    }

    @Override
    public void save(TournamentDTO dto) {

    }

    private Optional<TournamentDTO> resultSetToTournamentDTO(ResultSet res) throws SQLException {
        if (res.next()) {
            return Optional.of(new TournamentDTO(
                    res.getObject("uuid", UUID.class),
                    null,
                    null,
                    res.getInt("size")
            ));
        }
        return Optional.empty();
    }
}
