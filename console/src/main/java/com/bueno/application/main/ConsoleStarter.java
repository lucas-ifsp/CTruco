package com.bueno.application.main;

import com.bueno.application.main.commands.InitialMenuPrinter;
import com.bueno.application.main.commands.ExecuteMenu;
import com.bueno.application.withbots.features.ConsoleTournament;
import com.bueno.domain.usecases.bot.providers.BotManagerService;
import com.bueno.domain.usecases.bot.providers.RemoteBotApi;
import com.bueno.domain.usecases.tournament.converter.MatchConverter;
import com.bueno.domain.usecases.tournament.converter.TournamentConverter;
import com.bueno.domain.usecases.tournament.repos.FakeMatchRepository;
import com.bueno.domain.usecases.tournament.repos.FakeTournamentRepository;
import com.bueno.domain.usecases.tournament.repos.MatchRepository;
import com.bueno.domain.usecases.tournament.repos.TournamentRepository;
import com.bueno.domain.usecases.tournament.usecase.*;
import com.bueno.domain.usecases.user.UserRepository;
import com.bueno.persistence.DataBaseBuilder;
import com.bueno.persistence.repositories.RemoteBotRepositoryImpl;
import com.bueno.persistence.repositories.UserRepositoryImpl;
import com.remote.RemoteBotApiAdapter;

import java.sql.SQLException;
import java.util.List;

public class ConsoleStarter {

    public static void main(String[] args) throws SQLException {
        DataBaseBuilder dataBaseBuilder = new DataBaseBuilder();
        dataBaseBuilder.buildDataBaseIfMissing();
        RemoteBotRepositoryImpl remoteBotRepository = new RemoteBotRepositoryImpl();
        TournamentRepository tournamentRepository = new FakeTournamentRepository();
        MatchRepository matchRepository = new FakeMatchRepository();

        RemoteBotApi api = new RemoteBotApiAdapter();
        BotManagerService provider = new BotManagerService(remoteBotRepository, api);

        GetMatchUseCase getMatchUseCase = new GetMatchUseCase(matchRepository);
        UpdateMatchUseCase updateMatchUseCase = new UpdateMatchUseCase(matchRepository);
        UpdateTournamentUseCase updateTournamentUseCase = new UpdateTournamentUseCase(tournamentRepository, matchRepository, updateMatchUseCase);
        SaveTournamentUseCase saveTournamentUseCase = new SaveTournamentUseCase(tournamentRepository);
        SaveMatchUseCase saveMatchUseCase = new SaveMatchUseCase(matchRepository);
        CreateTournamentUseCase tournamentProvider = new CreateTournamentUseCase(saveTournamentUseCase, saveMatchUseCase);
        PlayTournamentMatchesUseCase playUseCase = new PlayTournamentMatchesUseCase(tournamentRepository,
                remoteBotRepository,
                api,
                provider,
                getMatchUseCase,
                updateTournamentUseCase,
                updateMatchUseCase);

        RefreshTournamentUseCase refreshUseCase = new RefreshTournamentUseCase(tournamentRepository,
                getMatchUseCase,
                updateTournamentUseCase,
                updateMatchUseCase);


        ConsoleTournament consoleTournament = new ConsoleTournament(tournamentProvider,
                playUseCase,
                refreshUseCase);
        consoleTournament.startTournament(List.of("LazyBot",
                "DummyBot",
                "MineiroByBueno",
                "VapoBot",
                "UncleBobBot",
                "SkolTable",
                "VeioDoBarBot",
                "W'rkncacnter"), 31);
        consoleTournament.tournamentMenu();


        //TODO - fazer jogador escolher as partidas a serem jogadas
//        TournamentDTO dto = tournamentProvider.createTournament(provider.providersNames(), 16);
//        System.out.println(dto);
//        dto = prepareTournamentUseCase.prepareMatches(dto);
//        System.out.println(dto);
//        dto = playUseCase.playAll(dto);
//        System.out.println(dto);
//        dto = refreshUseCase.refresh(dto);
//        System.out.println(dto);
//        playUseCase.getAllAvailableMatches(dto)
//                .forEach(matchDTO ->
//                        System.out.println("Match number: " +
//                                           matchDTO.matchNumber() +
//                                           " " +
//                                           matchDTO.p1Name() +
//                                           " VS " +
//                                           matchDTO.p2Name()));
//        Scanner scanner = new Scanner(System.in);
//        final int playMatchNumber = scanner.nextInt();
//        final UUID matchUuid = FindMatchUseCase.findUuidByMatchNumber(dto, playMatchNumber);
//        dto = playUseCase.playOne(dto, matchUuid);
//        dto = refreshUseCase.refresh(dto);
//        System.out.println(dto);


        ConsoleStarter console = new ConsoleStarter();
//        console.printInitialMenu();
//        console.menu();
    }

    private void printInitialMenu() {
        InitialMenuPrinter init = new InitialMenuPrinter();
        init.execute();
    }

    private void menu() {
        UserRepository userRepository = new UserRepositoryImpl();
        RemoteBotRepositoryImpl RemoteBotRepository = new RemoteBotRepositoryImpl();
        ExecuteMenu printer = new ExecuteMenu(RemoteBotRepository, new RemoteBotApiAdapter());
        printer.execute();
    }
}
