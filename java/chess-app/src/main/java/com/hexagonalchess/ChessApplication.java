package com.hexagonalchess;

import com.hexagonalchess.crosscontext.GameplayGameInitializer;
import com.hexagonalchess.gameplay.*;
import com.hexagonalchess.organizing.*;
import com.hexagonalchess.usermanagement.*;
import com.hexagonalchess.usermanagementorganizing.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.hexagonalchess.gameplay",
    "com.hexagonalchess.organizing",
    "com.hexagonalchess.usermanagement"
})
public class ChessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChessApplication.class, args);
    }

    // — Gameplay —

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

    // — Cross-context: Gameplay ↔ Organizing —

    @Bean
    public GameInitializer gameInitializer(SetupBoard setupBoard) {
        return new GameplayGameInitializer(setupBoard);
    }

    // — Organizing Games —

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

    // — User Management —

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public FriendshipRepository friendshipRepository() {
        return new InMemoryFriendshipRepository();
    }

    @Bean
    public CreateUser createUser(UserRepository userRepository) {
        return new CreateUser(userRepository);
    }

    @Bean
    public ListUsers listUsers(UserRepository userRepository) {
        return new ListUsers(userRepository);
    }

    @Bean
    public GetUser getUser(UserRepository userRepository) {
        return new GetUser(userRepository);
    }

    @Bean
    public UpdateUser updateUser(UserRepository userRepository) {
        return new UpdateUser(userRepository);
    }

    @Bean
    public DeleteUser deleteUser(UserRepository userRepository) {
        return new DeleteUser(userRepository);
    }

    @Bean
    public AddFriend addFriend(FriendshipRepository friendshipRepository) {
        return new AddFriend(friendshipRepository);
    }

    @Bean
    public RemoveFriend removeFriend(FriendshipRepository friendshipRepository) {
        return new RemoveFriend(friendshipRepository);
    }

    @Bean
    public ListFriends listFriends(FriendshipRepository friendshipRepository) {
        return new ListFriends(friendshipRepository);
    }

    // — Cross-context: User Management ↔ Organizing —

    @Bean
    public UserNameProvider userNameProvider(UserRepository userRepository) {
        return new UserManagementUserNameProvider(userRepository);
    }

    @Bean
    public FriendshipChecker friendshipChecker(FriendshipRepository friendshipRepository) {
        return new UserManagementFriendshipChecker(friendshipRepository);
    }

    // — Invitations —

    @Bean
    public InvitationRepository invitationRepository() {
        return new InMemoryInvitationRepository();
    }

    @Bean
    public CreateInvitation createInvitation(InvitationRepository invitationRepository,
                                             FriendshipChecker friendshipChecker) {
        return new CreateInvitation(invitationRepository, friendshipChecker);
    }

    @Bean
    public AcceptInvitation acceptInvitation(InvitationRepository invitationRepository,
                                             UserNameProvider userNameProvider,
                                             StartGame startGame) {
        return new AcceptInvitation(invitationRepository, userNameProvider, startGame);
    }

    @Bean
    public DeclineInvitation declineInvitation(InvitationRepository invitationRepository) {
        return new DeclineInvitation(invitationRepository);
    }

    @Bean
    public ListInvitations listInvitations(InvitationRepository invitationRepository) {
        return new ListInvitations(invitationRepository);
    }
}
