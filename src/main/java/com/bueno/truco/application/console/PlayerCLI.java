package com.bueno.truco.application.console;

import com.bueno.truco.domain.entities.deck.Card;
import com.bueno.truco.domain.entities.game.GameIntel;
import com.bueno.truco.domain.entities.game.GameRuleViolationException;
import com.bueno.truco.domain.entities.game.Round;
import com.bueno.truco.domain.entities.player.DummyPlayer;
import com.bueno.truco.domain.entities.player.Player;
import com.bueno.truco.domain.usecases.game.PlayGameUseCase;

import java.util.*;

public class PlayerCLI extends Player {

    public static void main(String[] args) {
        cls();
        Scanner scanner = new Scanner(System.in);

        System.out.println("====== LET'S TRUCO ======");

        System.out.print("Nome do jogador 1 > ");
        String name1 = scanner.nextLine();
        Player playerCLI1 = new PlayerCLI(name1);

        System.out.print("Nome do jogador 2 > ");
        String name2 = scanner.nextLine();
        Player playerCLI2 = new PlayerCLI(name2);


        PlayGameUseCase gameUseCase = new PlayGameUseCase(playerCLI1, playerCLI2);
        gameUseCase.play();
        scanner.close();
    }

    public PlayerCLI(String name) {
        super(name);
    }

    @Override
    public Card playCard() {
        Card cardToPlay = null;

        while (cardToPlay == null) {
            cls();
            printGameIntel();
            Scanner scanner = new Scanner(System.in);

            System.out.print("Carta a jogar [índice] > ");
            try {
                final int cardIndex = Integer.valueOf(scanner.nextLine()) - 1;

                if (cardIndex < 0 || cardIndex > cards.size() - 1) {
                    printErrorMessage("Valor inválido!");
                    continue;
                }

                System.out.print("Descartar [s, n] > ");
                final String choice = scanner.nextLine();
                if (isValidChoice(choice, "s", "n")){
                    printErrorMessage("Valor inválido!");
                    continue;
                }

                if (choice.equalsIgnoreCase("n"))
                    cardToPlay = cards.remove(cardIndex);
                else
                    cardToPlay = discard(cards.get(cardIndex));
            }catch (Exception e){
                printErrorMessage( "Valor inválido!");
                continue;
            }
        }
        return cardToPlay;
    }

    private boolean isValidChoice(String choice, String... options) {
        return Arrays.stream(options).noneMatch(option -> choice.equalsIgnoreCase(option));
    }

    @Override
    public boolean requestTruco() {
        cls();

        while(true) {
            printGameIntel();
            Scanner scanner = new Scanner(System.in);
            System.out.print("Pedir " + getNextHandValueAsString() + " [s, n]: ");
            final String choice = scanner.nextLine().toLowerCase();;

            if (isValidChoice(choice, "s", "n")) {
                printErrorMessage("Valor inválido!");
                continue;
            }
            return choice.equalsIgnoreCase("s")? true: false;
        }
    }

    @Override
    public int getTrucoResponse(int newHandPoints) {
        cls();
        while(true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(getGameIntel().getOpponentId(this) + " está pedindo "
                    + getNextHandValueAsString() + ". Escolha uma opção [(T)opa, (C)orre, (A)umenta]: ");

            final String choice = scanner.nextLine();

            if (isValidChoice(choice, "t", "c", "a")){
                printErrorMessage("Valor inválido!");
                continue;
            }

            printGameIntel();
            return toIntChoice(choice);
        }
    }

    private int toIntChoice(String choice) {
        if (choice.equalsIgnoreCase("c"))
            return -1;
        if (choice.equalsIgnoreCase("a"))
            return 1;
        return 0;
    }

    private String getNextHandValueAsString() {
        return switch (getGameIntel().getCurrentHandPoints()) {
            case 1 -> "truco";
            case 3 -> "seis";
            case 6 -> "nove";
            case 9 -> "doze";
            default -> throw new GameRuleViolationException("Invalid hand value!");
        };
    }

    private void printGameIntel() {
        final GameIntel intel = getGameIntel();

        System.out.println("+=======================================+");
        printGameMainInfo(intel);
        printRounds(intel);
        printCardsOpenInTable(intel);
        printVira(intel.getCurrentVira());
        printOpponentCardIfAvailable(intel);
        printOwnedCards();
        System.out.println("+=======================================+\n");
    }

    private void printGameMainInfo(GameIntel intel) {
        System.out.println(" Vez do: " + getId());
        System.out.println(" Ponto da mão: " + intel.getCurrentHandPoints());
        System.out.println(" Placar: " + getId() + " " + getScore() + " x " + intel.getOpponentScore(this) + " " + intel.getOpponentId(this));
    }

    private void printRounds(GameIntel intel) {
        final List<Round> roundsPlayed = intel.getRoundsPlayed();
        if (roundsPlayed.size() > 0) {
            System.out.print(" Ganhadores das Rodadas: | ");
            roundsPlayed.stream()
                    .map(Round::getWinner)
                    .forEach(PlayerCLI::printRoundResult);
            System.out.print("\n");
        }
    }

    private static void printRoundResult(Optional<Player> winner) {
        if (winner.isPresent())
            System.out.print(winner.get().getId() + " | ");
        else
            System.out.print(" Empate  |");
    }

    private void printCardsOpenInTable(GameIntel intel) {
        final Set<Card> openCards = intel.getOpenCards();
        if (openCards.size() > 0) {
            System.out.print(" Cartas na mesa: ");
            openCards.forEach(card -> System.out.print(card + " "));
            System.out.print("\n");
        }
    }

    private void printVira(Card vira) {
        System.out.println(" Vira: " + vira);
    }

    private void printOpponentCardIfAvailable(GameIntel intel) {
        final Optional<Card> cardToPlayAgainst = intel.getCardToPlayAgainst();
        if (cardToPlayAgainst.isPresent())
            System.out.println(" Carta do Oponente: " + cardToPlayAgainst.get());
    }

    private void printOwnedCards() {
        System.out.print(" Cartas na mão: ");
        for (int i = 0; i < cards.size(); i++) {
            System.out.print((i+1) + ") " + cards.get(i) + "\t");
        }
        System.out.print("\n");
    }

    private void printErrorMessage(String message) {
        Scanner scanner = new Scanner(System.in);
        cls();
        System.out.println(message);
        scanner.nextLine();
        cls();
    }

    private static void cls() {
        for (int i = 0; i < 15; ++i)
            System.out.println();
    }
}


