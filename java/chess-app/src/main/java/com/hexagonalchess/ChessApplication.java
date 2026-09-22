package com.hexagonalchess;

import com.hexagonalchess.crosscontext.GameplayGameInitializer;
import com.hexagonalchess.gameplay.*;
import com.hexagonalchess.organizing.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.hexagonalchess.gameplay",
    "com.hexagonalchess.organizing"
})
public class ChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChessApplication.class, args);
    }

    @Bean
    public BoardRepository boardRepository() {
        return new InMemoryBoardRepository();
    }

    @Bean
    public MoveRepository moveRepository() {
        return new InMemoryMoveRepository();
    }

    @Bean
    public SetupBoard setupBoard(BoardRepository boardRepository) {
        return new SetupBoard(boardRepository);
    }

    @Bean
    public MakeMove makeMove(BoardRepository boardRepository, MoveRepository moveRepository) {
        return new MakeMove(boardRepository, moveRepository);
    }

    @Bean
    public GameInitializer gameInitializer(SetupBoard setupBoard) {
        return new GameplayGameInitializer(setupBoard);
    }

    @Bean
    public GameRepository gameRepository() {
        return new InMemoryGameRepository();
    }

    @Bean
    public StartGame startGame(GameRepository gameRepository, GameInitializer gameInitializer) {
        return new StartGame(gameRepository, gameInitializer);
    }

    @Bean
    public GetGame getGame(GameRepository gameRepository) {
        return new GetGame(gameRepository);
    }

    @Bean
    public ListGames listGames(GameRepository gameRepository) {
        return new ListGames(gameRepository);
    }
}
