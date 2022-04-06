# CTruco - Truco game for didactic purpose

## Overview

This project was designed to serve as supporting material in classes involving object-oriented design and programming,
Java language, testing, and backend development. In the source code, students and practitioners will find implementations 
of concepts such as: 

- Modern Java programming (functional API, Streams, modules, switch expressions, etc.)
- Clean Code and Clean Architecture (SOLID);
- GRASP patterns;
- Design Patterns (Singleton, State, Strategy, DAO, etc.);
- Object Calisthenics;
- Testing Driven Development (TDD) using JUnit 5 and Mockito;
- Domain Driven Design (DDD).

## Downloading and Running

**CTruco** `domain` module was implemented using vanilla Java to exemplify the features the language provides. 
It isolates the core business rules from libraries and third party plugins, thus enabling the creation of applications in
different formats, for multiple platforms. The following playing modes are already available (Spring backend will be ready soon): 

- Console game between a player and a bot (run `cli/GameCLI` class in `console` module);
- Console simulation of games between two bots (run `standalone/PlayWithBots` class in `console` module); and
- JavaFX game between a player and a bot (run `view/WindowGameTable` class in `desktop` module);

The project uses Java 16 language features. Therefore, JDK 16+ is required. Further requirements are declared as Maven
dependencies, so no additional config is needed.  

## Project Modules

CTruco is composed of the following modules: 

- `domain:` encompasses game business rules, including core entities and use cases. All application modules depend on this module;
- `persistence:` provides concrete implementations for persistence interfaces specified in `domain` module use cases;
- `bot-spi:` contains a [Service Provider Interface (SPI)](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html) named `BotServiceProvider`, which enables any future bot implementation to be seamlessly integrated into `domain` use cases;
- `bot-impl:` provides two default implementations of `BotServiceProvider` interface: `DummyBot` (silly one), `MineiroBot` (not so silly);
- `console:` contains console versions of truco game applications that enable playing against bots or between bots;
- `desktop:`provides a JavaFX/FXML version of the truco game for users to play against bots;


## Testing

`Domain`, `bot-spi`, and `bot-impl` were developed using TDD and, therefore, are covered by several unit tests. In case of any change, 
please apply regression tests to assure proper code behaviour.

## Developing Your Own Bot Service

One of the ideas behind **CTruco** is to design a software flexible enough to receive new implementations of bot services provided by
the community without changing a single line of code. Implementing and integrating a bot into the game is straightforward:

1. Create a module with Maven;
2. Include a reference to the bot-spi module in the pom.xml file: 

    ```
   <dependency>
        <groupId>com.bueno</groupId>
        <artifactId>bot-spi</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
   ```

3. Create a `module-info.java` file under your `Java` folder and describe your module as follows: 
   ```
   module your.mod.name { // choose your module name
      requires bot.spi;
   }
   ```
   
4. Create a class to implement the interface `com.bueno.spi.service.BotServiceProvider` available in the `bot-spi` module; 

5. Update the `module-info.java` file to export and provide your implementation as service:
   ```
   module your.mod.name {
       requires bot.spi;
       exports name.of.the.package.where.your.service.implementation.is;
       provides com.bueno.spi.service.BotServiceProvider with YourServiceClass;
   }
   ```
   
6. In your `resources` folder, create a folder named `META-INF` and, inside the folder, create another folder named `services`. 
In the `services` folder, create a file named `com.bueno.spi.service.BotServiceProvider`, open it, and put the fully qualified 
name of your service implementation. For example: 

   ```
   name.of.the.package.where.your.service.implementation.is.YourServiceClass
   ```


7. Finally, add your module as a dependency in the `domain` module `pom.xml`, like this:

   ```
   <dependency>
      <groupId>com.bueno</groupId>
      <artifactId>bot-impl</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <scope>compile</scope>
   </dependency>
   ```

An example on how to implement a bot service can be found [here](https://github.com/lucas-ifsp/CTruco/tree/master/bot-impl). 

To check if you have configured it right, run the `standalone/PlayWithBots` class in `console` module. Your bot service implementation class should be available as a bot option.

Now that everything is set, you can develop the business logic of your bot service. The `BotServiceProvider` 
has four abstract methods to be implemented: 

- `int getRaiseResponse(GameIntel intel)`: answers a point raise request in a truco hand. Return value must be the following: -1 (quit), 0 (accept), 1 (re-raise/call);
- `getMaoDeOnzeResponse(GameIntel intel)`: choose if bot plays a *mão de onze*. Returning `false` means quit hand. Returning `true` means accept and play a three points hand;
- `boolean decideIfRaises(GameIntel intel)`: choose if bot starts a point raise request.  Returning `false` means do nothing. Returning `true` means requesting a point raise;
- `CardToPlay chooseCard(GameIntel intel)`: provided the card will be played or discarded in the current round.

There are only three model classes related to the service implementation:

- `GameIntel`: describes the current state of the game, including: bot cards, open cards in the table, vira, bot score, opponent score, etc.;
- `TrucoCard`: represents a valid card in the truco game, with fields such as CardRank and CardSuit;
- `CardToPlay`: wraps a TrucoCard as a card to be played in the round or discarded.


***THE FUNNY PART:  you can develop a bot to challenge other bots proposed by the community. If your bot is good enough, please pull request it.***



## License

This software was developed for non-commercial, didactic purposes. It is provided through [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.pt-br.html).

## Contributors

**CTruco** was initially designed and developed by **Prof. Lucas Oliveira** @ Federal Institute of São Paulo (IFSP) at São Carlos.






